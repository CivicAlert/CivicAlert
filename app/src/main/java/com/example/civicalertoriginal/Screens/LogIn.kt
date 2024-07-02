import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.civicalertoriginal.Components.*
import com.example.civicalertoriginal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

@Composable
fun LogIn(navController: NavController) {
    val database = Firebase.database
    val myRef = database.getReference("Community members")
    val auth = FirebaseAuth.getInstance()

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

            Image(modifier = Modifier
                .size(50.dp, 55.dp)
                .clickable { /* Handle Google Sign-In */ },
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
