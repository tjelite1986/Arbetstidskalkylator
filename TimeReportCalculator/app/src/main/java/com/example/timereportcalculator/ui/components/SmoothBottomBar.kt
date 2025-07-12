package com.example.timereportcalculator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    barHeight: Dp = 70.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    indicatorColor: Color = MaterialTheme.colors.primary,
    selectedContentColor: Color = MaterialTheme.colors.onPrimary,
    unselectedContentColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight),
        elevation = 8.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background indicator
            val selectedIndex = items.indexOf(selectedItem)
            val density = LocalDensity.current
            
            val indicatorOffset by animateDpAsState(
                targetValue = with(density) {
                    // SpaceEvenly calculation: each item gets equal space
                    val screenWidthPx = LocalContext.current.resources.displayMetrics.widthPixels  
                    val screenWidthDp = (screenWidthPx / density.density).dp
                    val totalPadding = 16.dp // Row padding
                    val availableWidth = screenWidthDp - totalPadding
                    
                    // With SpaceEvenly, each item gets availableWidth / items.size space
                    val itemSpace = availableWidth.value / items.size
                    val indicatorWidth = 60f
                    
                    // Center of each item space
                    val itemCenterX = (selectedIndex * itemSpace) + (itemSpace / 2)
                    val indicatorStartX = itemCenterX - (indicatorWidth / 2)
                    
                    // Add back the left padding offset
                    (indicatorStartX + 8).dp // 8dp is left padding
                },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            
            // Animated indicator background
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .size(width = 60.dp, height = barHeight)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(indicatorColor)
                    .align(Alignment.CenterStart)
            )
            
            // Navigation items
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
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
}

@Composable
private fun SmoothBottomBarItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedContentColor: Color,
    unselectedContentColor: Color
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) selectedContentColor else unselectedContentColor,
        animationSpec = tween(300)
    )
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val animatedWeight by animateFloatAsState(
        targetValue = if (isSelected) FontWeight.Bold.weight.toFloat() else FontWeight.Normal.weight.toFloat(),
        animationSpec = tween(300)
    )
    
    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = animatedColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(animatedScale)
            )
            
            if (isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.title,
                    color = animatedColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(animatedWeight.toInt()),
                    maxLines = 1
                )
            }
        }
    }
}

// Preview version for easier testing
@Composable
fun SmoothBottomBarPreview() {
    val items = listOf(
        NavigationItem.TimeReport,
        NavigationItem.Statistics, 
        NavigationItem.Templates,
        NavigationItem.Export,
        NavigationItem.Settings
    )
    
    var selectedItem by remember { mutableStateOf(items[0]) }
    
    MaterialTheme {
        SmoothBottomBar(
            items = items,
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it }
        )
    }
}