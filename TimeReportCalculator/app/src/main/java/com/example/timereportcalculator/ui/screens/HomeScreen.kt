package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val uriHandler = LocalUriHandler.current
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Välkommen till Tidrapportskalkylatorn!",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Din kompletta guide för löneberäkningar inom handel och lager",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        item {
            // About section
            InfoCard(
                title = "Om Tidrapportskalkylatorn",
                content = "Tidrapportskalkylatorn är ett kostnadsfritt verktyg som hjälper dig att snabbt och enkelt beräkna din lön, inklusive OB-tillägg och andra tillägg som du har rätt till enligt gällande kollektivavtal. Verktyget är speciellt anpassat för dig som arbetar inom butik och lager, och följer reglerna i Handelsanställdas förbunds kollektivavtal.\n\nOavsett om du arbetar deltid eller heltid, kvällar eller helger, så kan du enkelt få en överblick över din förväntade lön och vilka tillägg du bör få för dina arbetstider."
            )
        }
        
        item {
            // Features section
            FeatureCard(
                title = "Vad kan appen hjälpa dig med?",
                features = listOf(
                    FeatureItem(Icons.Default.Calculate, "Grundlöneberäkning", "Beräkna din grundlön baserat på dina arbetstider och timlön"),
                    FeatureItem(Icons.Default.NightlightRound, "OB-tillägg", "Automatisk beräkning av kvälls-, natt-, helg- och söndagstillägg"),
                    FeatureItem(Icons.Default.CalendarToday, "Semester och semesterlön", "Få översikt över intjänad semester och semesterersättning"),
                    FeatureItem(Icons.Default.Settings, "Flexibla beräkningar", "Anpassa beräkningarna efter dina specifika arbetstider"),
                    FeatureItem(Icons.Default.TouchApp, "Enkelt att använda", "Intuitiv design som gör beräkningarna snabba och tydliga")
                )
            )
        }
        
        item {
            // Rights and information section
            InfoCard(
                title = "Viktig Information för Dig som Anställd",
                content = "Som anställd har du rättigheter och det är viktigt att du känner till dem. Här nedan hittar du information som kan vara till stor hjälp om du har frågor kring ditt arbete, lön, arbetstider eller andra anställningsvillkor."
            )
        }
        
        item {
            // Collective agreements
            InfoExpandableCard(
                title = "🤝 Kollektivavtal",
                content = "Kollektivavtalen är överenskommelser mellan fackförbund och arbetsgivare som reglerar anställningsvillkor som lön, arbetstider, OB-tillägg, semester, sjukfrånvaro och uppsägningstider. De är grunden för din trygghet och rättigheter på jobbet.",
                details = listOf(
                    "För dig som jobbar i butik eller på lager/e-handel gäller oftast Handelns Kollektivavtal mellan Handelsanställdas förbund och Svensk Handel.",
                    "I avtalet hittar du information om minimilöner, arbetstidsregler, OB-tillägg och mycket mer."
                ),
                linkText = "Handelns Kollektivavtal",
                linkUrl = "https://www.handels.se/dina-avtal/handelns-kollektivavtal/"
            )
        }
        
        item {
            // Trade unions
            InfoExpandableCard(
                title = "👥 Fackförbund",
                content = "Ett fackförbund arbetar för att skydda och förbättra medlemmarnas anställningsvillkor. Som medlem får du juridiskt stöd, rådgivning vid konflikter, och hjälp med löneförhandlingar. Medlemskapet ger dig också en starkare röst på arbetsplatsen.",
                details = listOf(
                    "För anställda inom butik och lager är det primära fackförbundet Handelsanställdas förbund.",
                    "Andra relevanta fackförbund kan vara Unionen (för tjänstemän) eller LO (Landsorganisationen i Sverige).",
                    "Som medlem får du tillgång till juridisk rådgivning, löneförhandlingar och stöd vid arbetsplatskonfliker."
                ),
                linkText = "Handelsanställdas förbund",
                linkUrl = "https://www.handels.se/"
            )
        }
        
        item {
            // Unemployment insurance
            InfoExpandableCard(
                title = "🛡️ A-kassa (Arbetslöshetskassa)",
                content = "A-kassan ger dig ekonomiskt skydd vid arbetslöshet genom inkomstrelaterad ersättning. Det är viktigt att vara medlem i en a-kassa för att få bättre ekonomisk trygghet om du skulle bli arbetslös. Ersättningen är betydligt högre än grundersättningen från Arbetsförmedlingen.",
                details = listOf(
                    "Om du är medlem i Handelsanställdas förbund kan du ansluta dig till Handels a-kassa.",
                    "Kom ihåg att det finns karenstider, så det är bra att bli medlem i god tid innan du eventuellt behöver det."
                ),
                linkText = "Handels a-kassa",
                linkUrl = "https://handelsakassa.se/"
            )
        }
        
        item {
            // Useful resources
            ResourcesCard()
        }
        
        item {
            // Disclaimer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ Viktigt att veta",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.error
                    )
                    Text(
                        text = "Tidrapportskalkylatorn är ett hjälpverktyg för uppskattningar. Den ersätter inte professionell rådgivning eller officiella lönebesked från din arbetsgivare. Kontrollera alltid dina slutliga lönebesked och tveka inte att kontakta ditt fackförbund om du har frågor om dina rättigheter eller lönevillkor.",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = content,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    features: List<FeatureItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            
            features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = feature.title,
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = feature.description,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoExpandableCard(
    title: String,
    content: String,
    details: List<String>,
    linkText: String,
    linkUrl: String
) {
    var expanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Dölj detaljer" else "Visa detaljer"
                    )
                }
            }
            
            Text(
                text = content,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            if (expanded) {
                details.forEach { detail ->
                    Text(
                        text = "• $detail",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )
                }
                
                Button(
                    onClick = { uriHandler.openUri(linkUrl) },
                    modifier = Modifier.padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        text = linkText,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ResourcesCard() {
    val uriHandler = LocalUriHandler.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📋 Användbara Resurser",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            
            val resources = listOf(
                "Arbetsmiljöverket" to "https://www.arbetsmiljoverket.se/",
                "Diskrimineringsombudsmannen (DO)" to "https://www.do.se/",
                "Arbetsförmedlingen" to "https://www.arbetsformedlingen.se/",
                "Skatteverket" to "https://www.skatteverket.se/"
            )
            
            resources.forEach { (name, url) ->
                TextButton(
                    onClick = { uriHandler.openUri(url) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = name,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}

private data class FeatureItem(
    val icon: ImageVector,
    val title: String,
    val description: String
)