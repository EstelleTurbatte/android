package fr.isen.turbatte.androidtoolbox

import android.icu.util.TimeUnit

enum class BLEUUIDAttributes(val uuid: String, val title: String) {
    SERVICE_INCONNU("", "Inconnu"),
    GENERIC_ACCESS("00001800-0000-1000-8000-00805f9b34fb", "Accès générique"),
    GENERIC_ATTRIBUTE("00001801-0000-1000-8000-00805f9b34fb", "Attribut générique"),
    CUSTOM_SERVICE("466c1234-f593-11e8-8eb2-f2801f1b9fd1", "Service Spécifique"),
    DEVICE_NAME("00002a00-0000-1000-8000-00805f9b34fb", "Nom du périphérique"),
    APPEARANCE("00002a01-0000-1000-8000-00805f9b34fb", "Apparance"),
    CUSTOM_CHARACTERISTIC("466c9abc-f593-11e8-8eb2-f2801f1b9b34fb", "Caractéristique Spécifique"),
    CUSTOM_CHARACTERISTIC_2("466c9abc-f593-11e8-8eb2-f2801f1b9b34fb", "Caractéristique Spécifique");

    companion object {
        fun getBLEAttributeFromUUID(uuid: String) =
            values().firstOrNull { it.uuid == uuid } ?: SERVICE_INCONNU

    }
}