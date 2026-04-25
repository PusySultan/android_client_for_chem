package com.example.catod_ble.Pages

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.catod_ble.R
import com.example.catod_ble.data.Data

class BLEActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContent{

            val isback = remember {
                mutableStateOf(false)
            }

               Column(modifier = Modifier
                   .background(colorResource(id = R.color.Blue_Black_v1)),
               )
               {

                   Box(modifier = Modifier
                       .height(50.dp))

                   Box(modifier = Modifier
                       .fillMaxWidth()
                       .height(25.dp),
                       contentAlignment = Alignment.Center)
                   {
                       Text(text = "Доступные устройства",
                           fontSize = 15.sp,
                           style = TextStyle(color = Color.White))

                   }

                   Box(modifier = Modifier
                       .height(30.dp))

                   LazyColumn (
                       horizontalAlignment =  Alignment.CenterHorizontally,
                       modifier = Modifier
                           .fillMaxSize()
                           .background(Color.Black)
                           .padding(top = 10.dp)

                   )
                   {
                       itemsIndexed(Data.NameBLEarray)
                       {
                               index, el -> Item(title = Data.NameBLEarray[index],index, isback)

                           if(isback.value)
                           {
                               val i = Intent()
                               i.putExtra("mac", Data.choosingMac)
                               setResult(RESULT_OK, i)
                               isback.value = true
                               finish()
                           }
                       }
                   }
               }
        }

    }

}

@Composable
fun Item(title: String, index: Int, isBack: MutableState<Boolean>)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(bottom = 10.dp),
        colors = CardColors(
            containerColor = colorResource(id = R.color.purple_200),
            contentColor = Color.White,
            disabledContainerColor = Color.Yellow,
            disabledContentColor = Color.Red
        ),
        onClick = {
            if(Data.choosingMac == "")
            {
                Data.choosingMac = Data.MacBLEarray[index] // Устанавливаем нужный мак адрес
                isBack.value = true // Переменную выхода назначаем в тру
                Data.StopCorutin = false // Корутин не останавливаем
            }
            else{
                Data.choosingMac = "" // Удаляем мак адрес
                Data.StopCorutin = true // Останавливаем корутину
                isBack.value = true // выходим из данного активити
                Data.BLEconnected = false // разрываем подключение
            }
        }
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Box(Modifier
                .fillMaxWidth(0.3f),
                contentAlignment = Alignment.Center
            )
            {
                Icon(modifier = Modifier
                    .size(20.dp),
                    painter = painterResource(id = R.drawable.bluetooth),

                    tint = if(Data.BLEconnected && Data.choosingMac == Data.MacBLEarray[index])
                    {
                        Color.Green
                    }
                    else{
                        Color.White
                    },

                    contentDescription = "" )
            }
            Box(Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            {
                Text(text = title,
                    fontSize = 15.sp
                )
            }
        }
    }
}

