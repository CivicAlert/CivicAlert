
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.BottomButtons
import com.example.civicalertoriginal.Components.EmailTextFields
import com.example.civicalertoriginal.Components.InstructionText
import com.example.civicalertoriginal.Components.LogAndForgotHeader
import com.example.civicalertoriginal.Components.PasswordTextFields
import com.example.civicalertoriginal.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

@Composable
fun LogIn(navController: NavController) {
    val database = Firebase.database
    val myRef = database.getReference("Community members")
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    // Configure Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.google_id)) // Your client ID from google-services.json
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Google Sign-In was successful, authenticate with Firebase
                account?.idToken?.let { idToken ->
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show()
                                navController.navigate("Dashboard")
                            } else {
                                Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Log.e("LogIn", "Google Sign-In failed", e)
                Toast.makeText(context, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Surface(
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 80.dp)
                .fillMaxSize()
        ) {
            LogAndForgotHeader(screenLabel = "Login")
            val context = LocalContext.current

            Spacer(modifier = Modifier.size(40.dp))
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Spacer(modifier = Modifier.padding(10.dp))

            fun validateEmail(email: String): Boolean {
                val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
                return Pattern.matches(emailPattern, email)
            }

            fun validatePassword(password: String): Boolean {
                return password.length >= 6
            }

            EmailTextFields(value = email, onChange = {
                if (email.length <= 100) {
                    email = it
                    validateEmail(email)
                }
            }, fieldLabel = "Enter Email Address")

            Spacer(modifier = Modifier.size(10.dp))
            PasswordTextFields(value = password, onChange = {
                if (password.length <= 100) {
                    password = it
                    validatePassword(password)
                }
            }, fieldLabel = "Enter your password")

            Spacer(modifier = Modifier.padding(6.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .clickable { navController.navigate("ForgotPassword") }
                        .padding(end = 25.dp),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    text = "Register",
                    modifier = Modifier
                        .clickable { navController.navigate("registration") }
                        .padding(start = 25.dp),
                    style = TextStyle(fontStyle = FontStyle.Normal, color = Color.Blue)
                )
            }
            InstructionText(value = "Sign with google")


            Image(
                modifier = Modifier
                    .size(50.dp, 55.dp)
                    .clickable {
                        val signInIntent = googleSignInClient.signInIntent
                        signInLauncher.launch(signInIntent)
                    },
                painter = painterResource(id = R.drawable.googlepic),
                contentDescription = "Google SignIn"
            )

            Spacer(modifier = Modifier.size(18.dp))

            BottomButtons(name = "Sign in") {
                Log.d("LogIn", "Email: $email, Password: $password")
                if (validateEmail(email) && validatePassword(password)) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show()
                                navController.navigate("Dashboard")
                            } else {
                                Log.e("LogIn", "Authentication failed", task.exception)
                                Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Preview
@Composable
fun LogInPreview() {
    val navController = rememberNavController()
    LogIn(navController)
}