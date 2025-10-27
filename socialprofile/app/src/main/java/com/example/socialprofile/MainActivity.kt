package com.example.socialprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialProfileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun SocialProfileTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF8A38F5),
        onPrimary = Color.White,
        secondary = Color(0xFFAB47BC),
        onSecondary = Color.White,
        error = Color(0xFFD32F2F),
        background = Color.White,
        onBackground = Color(0xFF1C1B1F),
        surface = Color.White,
        onSurface = Color(0xFF1C1B1F)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var isFollowing by remember { mutableStateOf(false) }
    var likes by remember { mutableStateOf(120000) }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp)
                .padding(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            // Info profile (image, nama, nim)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // foto profil
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFF25475F), CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.karina),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // nama
                Text(
                    text = "Karina Aespa",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // NIM
                Text(
                    text = "235150700111000",
                    fontSize = 14.sp,
                    color = Color(0xFF000000)
                )
            }

            Spacer(modifier = Modifier.height(46.dp))

            // middle; likes + follow button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // likes
                Row(

                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Likes",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.US).format(likes)} Likes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF000000)
                    )
                }

                Spacer(modifier = Modifier.height(46.dp))

                //button follow unfollow
                Button(
                    onClick = { isFollowing = !isFollowing },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(43.dp),
                    colors = if (isFollowing) {
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = Color.White
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = if (isFollowing) {
                        androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else null
                ) {
                    Text(
                        text = if (isFollowing) "Unfollow" else "Follow",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Status Text
                Text(
                    text = if (isFollowing) {
                        "Anda telah mengikuti akun ini"
                    } else {
                        "Ingin mengikuti akun ini?"
                    },
                    fontSize = 16.sp,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(154.dp))

            // extension haters
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Extension Haters (Hack Jumlah Like)",
                    fontSize = 14.sp,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // + button
                    FilledIconButton(
                        onClick = { likes += 1 },
                        modifier = Modifier
                            .width(106.dp)
                            .height(66.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Tambah likes",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))

                    // - button
                    OutlinedIconButton(
                        onClick = {
                            val newLikes = likes - 1
                            if (newLikes >= 0) {
                                likes = newLikes
                            }
                        },
                        enabled = likes > 0,
                        modifier = Modifier
                            .width(106.dp)
                            .height(66.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.outlinedIconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = Color.Gray
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (likes > 0) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text(
                            text = "−",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = if (likes > 0) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SocialProfileTheme {
        ProfileScreen()
    }
}