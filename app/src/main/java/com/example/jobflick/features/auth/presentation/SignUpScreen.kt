package com.example.jobflick.features.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.components.JFInputField
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.Jost
import com.example.jobflick.features.onboarding.presentation.RoleSelectionScreen

@Composable
fun SignUpScreen(
    onSubmit: (email: String, password: String) -> Unit,
    onClickSignIn: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val emailOk = '@' in email && '.' in email
    val passOk = pass.length >= 8
    val confirmOk = pass == confirm
    val formOk = emailOk && passOk && confirmOk

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter = painterResource(R.drawable.wavy_left_icon_header)
        val ratio = if (painter.intrinsicSize.isUnspecified) 16f / 9f
        else painter.intrinsicSize.width / painter.intrinsicSize.height

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            contentScale = ContentScale.FillWidth
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Daftar",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = Jost,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Text(
            "Buat akun untuk masuk.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = Jost,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(20.dp))

        // form
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(start = 24.dp, bottom = 6.dp, end = 24.dp)) {
            Text("Email", fontFamily = Jost, fontSize = 14.sp, modifier = Modifier
                .fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            JFInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Masukkan alamat email"
            )

            Spacer(Modifier.height(14.dp))

            Text("Password", fontFamily = Jost, fontSize = 14.sp, modifier = Modifier
                .fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            JFInputField(
                value = pass,
                onValueChange = { pass = it },
                placeholder = "Min. 8 karakter",
                isPassword = true
            )

            Spacer(Modifier.height(14.dp))

            Text("Ulangi Password", fontFamily = Jost, fontSize = 14.sp, modifier = Modifier
                .fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            JFInputField(
                value = confirm,
                onValueChange = { confirm = it },
                placeholder = "Ketik ulang password",
                isPassword = true
            )
        }

        Spacer(Modifier.height(48.dp))

        // button
        JFPrimaryButton(
            text = "Daftar",
            backgroundColor = if (formOk) BluePrimary else BluePrimary.copy(alpha = 0.25f),
            onClick = { if (formOk) onSubmit(email, pass) }
        )

        Spacer(Modifier.height(16.dp))

        // sdh pnya akun
        val annotated = buildAnnotatedString {
            append("Sudah punya akun? ")
            withStyle(SpanStyle(color = BluePrimary, fontWeight = FontWeight.Medium)) {
                append("Masuk")
            }
        }
        Text(
            text = annotated,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickSignIn() }
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = Jost)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpPreview() {
    Surface {
        SignUpScreen(
            onSubmit = { _, _ -> },
            onClickSignIn = {}
        )
    }
}
