package com.example.timereportcalculator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.navigation.NavigationItem

@Composable
fun SmoothBottomBar(
    items: List<NavigationItem>,
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    barHeight: Dp = 72.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    selectedContentColor: Color = MaterialTheme.colors.primary,
    unselectedContentColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
) {
    // Clean and professional surface
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight),
        elevation = 8.dp,
        color = backgroundColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                SmoothBottomBarItem(
                    item = item,
                    isSelected = item == selectedItem,
                    onClick = { onItemSelected(item) },
                    selectedContentColor = selectedContentColor,
                    unselectedContentColor = unselectedContentColor
                )
            }
        }
    }
}

@Composable
private fun SmoothBottomBarItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedContentColor: Color,
    unselectedContentColor: Color
) {
    // Simple and clean color animation
    val animatedIconColor by animateColorAsState(
        targetValue = if (isSelected) selectedContentColor else unselectedContentColor,
        animationSpec = tween(200)
    )
    
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) selectedContentColor else unselectedContentColor,
        animationSpec = tween(200)
    )
    
    // Natural background indicator
    val animatedBackgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.12f else 0f,
        animationSpec = tween(200)
    )
    
    // Clean clickable item with natural indicator
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(56.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                selectedContentColor.copy(alpha = animatedBackgroundAlpha)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    radius = 28.dp,
                    color = selectedContentColor.copy(alpha = 0.2f)
                )
            ) { onClick() }
            .padding(8.dp)
    ) {
        // Clean icon display
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.icon,
            contentDescription = item.title,
            tint = animatedIconColor,
            modifier = Modifier.size(24.dp)
        )
        
        // Always show label but animate its appearance
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = item.title,
            color = animatedTextColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

// Enhanced preview with better demo
@Composable
fun SmoothBottomBarPreview() {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.TimeReport,
        NavigationItem.Statistics, 
        NavigationItem.WeeklySchedule,
        NavigationItem.Export,
        NavigationItem.Settings
    )
    
    var selectedItem by remember { mutableStateOf(items[0]) }
    
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            // Spacer to push bottom bar to bottom
            Spacer(modifier = Modifier.weight(1f))
            
            // Enhanced bottom bar with improved styling
            SmoothBottomBar(
                items = items,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}