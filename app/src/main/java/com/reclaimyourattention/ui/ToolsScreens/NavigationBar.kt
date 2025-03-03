package com.reclaimyourattention.ui.ToolsScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.reclaimyourattention.ui.MainScreen
import com.reclaimyourattention.ui.ToolsScreen
import com.reclaimyourattention.ui.UsageScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationBar(){
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Tools", Icons.Default.Build),
        NavItem("Usage", Icons.Default.Analytics),
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(modifier= Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar{
                navItemList.forEachIndexed{index, navItem ->
                    NavigationBarItem(
                        selected= selectedIndex == index ,
                        onClick = {
                            selectedIndex = index
                        },
                        icon ={
                            Icon(imageVector=navItem.icon,contentDescription="Icon")
                        },
                        label = { Text(text= navItem.label)}
                    )
                }

            }
        }) { innerPadding ->
            ContentScreen(modifier=Modifier.padding(innerPadding),selectedIndex)

    }
}

@Composable
fun ContentScreen(modifier: Modifier=Modifier, selectedIndex : Int){
    when(selectedIndex){
        0-> MainScreen()
        1-> ToolsScreen()
        2-> UsageScreen()
    }
}


@Preview(showSystemUi = true)
@Composable
fun Preview(){
    NavigationBar()
}
