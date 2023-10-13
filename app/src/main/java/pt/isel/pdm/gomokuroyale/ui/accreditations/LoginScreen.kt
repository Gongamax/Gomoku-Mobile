package pt.isel.pdm.gomokuroyale.ui.accreditations


import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val BACKGROUND = 0xFFFF4FC3F7
const val TEXT_BOX = 0xFFBDBDBD

@Composable
fun TextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic
        ), color = Color.Black,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationBox(
    text: String,
    value: String,
    resourceId: Int
) {

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp)),
        value = value,
        onValueChange = {},
        label = { Text(text = text, fontStyle = FontStyle.Italic, color = Color(TEXT_BOX)) },

        leadingIcon = {
            Icon(
                painter = painterResource(id = resourceId),
                contentDescription = "",
                modifier = Modifier
                    .height(24.dp)
                    .padding(4.dp)
            )
        }
    )
}


@Composable
fun VerificationComponent(text : String, onClick : () -> Unit) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 5.dp)
            .clickable { onClick() },
        color = Color.Gray,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline

    )
}

@Composable
fun ButtonComponent(value: String) {
    Button(
        onClick = { /*TODO*/ }, modifier = Modifier.heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Cyan, Color.Blue)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }


}

@Composable
fun DivideComponent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), color = Color.Gray, thickness = 1.dp
        )
        Text(text = "or", modifier = Modifier.padding(horizontal = 10.dp), color = Color.Gray)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), color = Color.Gray, thickness = 1.dp
        )
    }
}

@Composable
private fun AlternativeLogin(text: String, @DrawableRes id: Int, onClick: () -> Unit) {

    OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(BACKGROUND))
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = id),
                contentDescription = null,
                modifier = Modifier.sizeIn(maxWidth = 32.dp)
            )
            Text(
                text = text,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    GomokuRoyaleTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(BACKGROUND))
                .padding(35.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                TextComponent("LOGIN")
                Spacer(modifier = Modifier.height(100.dp))
                InformationBox(text = "Enter Email...", value = "", resourceId = R.drawable.email)
                Spacer(modifier = Modifier.height(10.dp))
                InformationBox(text = "Password", value = "", resourceId = R.drawable.password)
                Spacer(modifier = Modifier.height(20.dp))
                VerificationComponent("Forgot Password?", onClick = {})
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(value = "Login")
                Spacer(modifier = Modifier.height(20.dp))
                DivideComponent()
                Spacer(modifier = Modifier.height(40.dp))
                VerificationComponent(text = "Don't have an account?", onClick = {})
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AlternativeLogin(text = "Google", id = R.drawable.google, onClick = {})
                    AlternativeLogin(text = "Facebook", id = R.drawable.facebook, onClick = {})
                }

            }


        }
    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    LoginScreen()
}