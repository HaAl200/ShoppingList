package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                ShoppingListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen() {
    // erstellt eine zustandsbehaftete Liste von Einkaufsartikeln, die auch Konfigurationsänderungen wie Bildschirmrotation überlebt.
    var items by rememberSaveable { mutableStateOf(listOf<String>()) }

// erstellt eine zustandsbehaftete Zeichenkette für das Eingabefeld, ebenfalls überlebensfähig bei Konfigurationsänderungen.
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold( // Layout Toolkit von Jetpack Compose, damit man das App Layout auf dem Bildschirm einfach machen kann. (Mit Padding etc., wie htmll damals ungefähr.)
        // definiert die obere App-Leiste. (Top Bar)
        topBar = {
            TopAppBar(
                // setzt den Titel der App-Leiste auf "Shoping List".
                title = { Text("Shopping List") }
            )
        },
        // definiert dien Button zum Hinzufügen von Dingen. (Floating Action Button)
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // prüft, ob das Textfeld nicht leer ist. (keine Leerzeichen)
                if (text.isNotBlank()) {
                    // fügt den Text als neuen Eintrag zur Liste hinzu.
                    items = items + text
                    // setzt das Textfeld nach dem Hinzufügen zurück.
                    text = ""
                }
            }) {
                // Zeigt ein Plus-Symbol auf dem Button.
                Icon(Icons.Filled.Add, "add new Item")
            }
        }
    ) { innerPadding ->
        // Definiert die Hauptspalte, in der die UI-Elemente angeordnet werden.
        Column(
            modifier = Modifier
                // wendet das innere Padding an, das von Scaffold bereitgestellt wird.
                .padding(innerPadding)
                // nutzt die gesamte verfügbare Höhe und Breite des Bildschirms.
                .fillMaxSize()
                // fügt zusätzlich bisschen Außenabstand für die Inhalte hinzu.
                .padding(16.dp)
        ) {
            // Eingabefeld für neuen Artikel. (vorgegeben.)
            OutlinedTextField(
                // Der aktuelle Textwert des Felds.
                value = text,
                // Callback, das aufgerufen wird, wenn sich der Text ändert.
                onValueChange = { text = it },
                // Beschriftung oberhalb des Feldes.
                label = { Text("new article") },
                // macht das Feld so breit wie möglich.
                modifier = Modifier.fillMaxWidth()
            )


            // zeigt eine Nachricht, wenn keine Artikel in der Liste sind.
            if (items.isEmpty()) {
                Text(
                    // Der Text, der angezeigt wird, wenn die Liste leer ist.
                    text = "Your Shopping list is empty. Add a new article with the button below.",
                    // Stil für größeren Text.
                    style = MaterialTheme.typography.bodyLarge,
                    // Zentriert den Text horizontal in der Spalte
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Scrollbare Liste, die nur sichtbare Elemente rendert. (Lazy Loading)
                LazyColumn {
                    // iteriert über die Artikel in der Liste.
                    items(items) { item ->
                        // zeigt ein einzelnes Listenelement an.
                        ShoppingListItem(
                            itemName = item,
                            // Callback für das Entfernen eines Artikels bei Klick.
                            onItemClick = {
                                items = items - item
                            }
                        )

                    }
                }
            }
        }
    }
}
@Composable
fun ShoppingListItem(itemName: String, onItemClick: () -> Unit, modifier: Modifier = Modifier) {
    // Textanzeige für einen Listeneintrag (Artikel).
    Text(
        // Der anzuzeigende Artikelname.
        text = itemName,
        // Zusätzliche Modifikationen.
        modifier = modifier
            // macht das Element so breit wie möglich.
            .fillMaxWidth()
            // reagiert auf Klicks. (zum Entfernen)
            .clickable { onItemClick() }
            .padding(vertical = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    ShoppingListTheme {
        ShoppingListScreen()
    }
}