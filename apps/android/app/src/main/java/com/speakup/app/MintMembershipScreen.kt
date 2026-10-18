package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Informational membership comparison until a verified Play Billing bridge and
 * backend-issued signed entitlements are ready. Never enables a fake purchase.
 */
@Composable
internal fun MintMembershipScreen(onReturn: () -> Unit) {
    var showBillingHelp by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("English Mint Membership", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Start speaking for free. Plus and Pro are future options — purchases are not enabled.")
        Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primaryContainer) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Your membership: Free", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Enjoy core lessons, curated games, private speaking practice and basic progress without a subscription.")
                Text("No payment method is needed.")
            }
        }
        MintMembershipPolicy.offers().forEach { offer ->
            Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(offer.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(offer.summary)
                    Text(if (offer.tier == MintTier.FREE) "Included now"
                        else "Coming later • pricing and availability to be confirmed",
                        color = MaterialTheme.colorScheme.primary)
                    if (offer.tier != MintTier.FREE) {
                        OutlinedButton(onClick = { showBillingHelp = true }, modifier = Modifier.fillMaxWidth()) {
                            Text("Why isn't this available?")
                        }
                    }
                }
            }
        }
        Text("Purchases, subscription restoration and cancellation must be connected to verified Google Play Billing products and a secure server before release.",
            style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = onReturn, modifier = Modifier.fillMaxWidth()) { Text("Return to Profile") }
    }
    if (showBillingHelp) AlertDialog(
        onDismissRequest = { showBillingHelp = false },
        title = { Text("Paid plans aren't available yet") },
        text = { Text("There is no verified Play Billing connection or published price. No money will be collected. Current Free games and speaking activities are available without payment.") },
        confirmButton = { TextButton(onClick = { showBillingHelp = false }) { Text("OK") } }
    )
}
