package pt.isel.pdm.gomokuroyale.authentication.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.authentication.ui.login.TEXT_BOX

const val BUTTON_COLOR = 0xFF7E91DB

@Composable
fun TextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(
            fontSize = 35.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace,
            fontStyle = FontStyle.Italic,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    )
}
//preciso uma variavel que guarde o valor
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationBox(
    text: String,
    value: String,
    onValueChange : (String) -> Unit,
    resourceId: Int
) {

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp)),
        value = value,
        onValueChange = onValueChange,
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
fun TextComponent(text: String, underline: Boolean, enableClick: Boolean,onClick: () -> Unit) {
    if (underline && enableClick) {
        Text(
            text,
            modifier = Modifier
                .clickable { onClick() },
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    } else {
        Text(
            text, color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun VerificationComponent(text: String?  = null, textUnderline: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement =  Arrangement.Center) {
        if (text != null) {
            TextComponent(text = text, underline = false, enableClick = false, onClick = {})
            Spacer(modifier = Modifier.width(5.dp))
            TextComponent(
                text = textUnderline,
                underline = true,
                enableClick = true,
                onClick = onClick
            )

        } else {
            TextComponent(
                text = textUnderline,
                underline = true,
                enableClick = true,
                onClick = onClick
            )
        }
    }


}

@Composable
fun ButtonComponent(iconResourceId: Int, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(1f),
        colors = ButtonDefaults.buttonColors( Color(BUTTON_COLOR))
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = "",
            modifier = Modifier
                .height(30.dp)
                .padding(4.dp),
            tint = Color.Black
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

//val a = LocalConfiguration.current.screenHeightDp

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
        Text(text = "or", modifier = Modifier.padding(horizontal = 10.dp), color = Color.Gray, fontStyle = FontStyle.Italic, fontSize = 20.sp)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), color = Color.Gray, thickness = 1.dp
        )
    }
}

@Composable
fun IconButtonWithBorder(
    iconResourceId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


