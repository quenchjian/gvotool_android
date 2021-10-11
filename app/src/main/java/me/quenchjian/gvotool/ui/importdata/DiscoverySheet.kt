package me.quenchjian.gvotool.ui.importdata

import me.quenchjian.gvotool.database.entity.Discovery

enum class DiscoverySheet(
  val sheetName: String,
  val type: Discovery.Type? = null,
  val columns: Array<String> = emptyArray(),
) {
  MEDAL("功績表"),
  HISTORIC_SITE("史跡", Discovery.Type.HISTORIC_SITE, arrayOf("U", "C", "G", "B", "I", "H", "A")),
  RELIGIOUS_ARCHITECTURE(
    "建築",
    Discovery.Type.RELIGIOUS_ARCHITECTURE,
    arrayOf("T", "C", "F", "B", "H", "G", "A")
  ),
  HISTORICAL_RELIC(
    "歷史",
    Discovery.Type.HISTORICAL_RELIC,
    arrayOf("U", "C", "G", "B", "I", "H", "A")
  ),
  RELIGIOUS_RELIC("宗教", Discovery.Type.RELIGIOUS_RELIC, arrayOf("U", "C", "G", "B", "I", "H", "A")),
  ARTWORK("美術", Discovery.Type.ARTWORK, arrayOf("T", "C", "G", "B", "I", "H", "A")),
  TREASURE("寶藏", Discovery.Type.TREASURE, arrayOf("U", "C", "G", "B", "I", "H", "A")),
  FOSSIL("化石", Discovery.Type.FOSSIL, arrayOf("S", "C", "F", "B", "H", "G", "A")),
  PLANT("植物", Discovery.Type.PLANT, arrayOf("T", "C", "G", "B", "I", "H", "A")),
  INSECT("昆蟲", Discovery.Type.INSECT, arrayOf("T", "C", "G", "B", "I", "H", "A")),
  BIRD("鳥類", Discovery.Type.BIRD, arrayOf("U", "C", "F", "B", "H", "G", "A")),
  SMALL_CREATURE("小型", Discovery.Type.SMALL_CREATURE, arrayOf("S", "C", "F", "B", "H", "G", "A")),
  MEDIUM_CREATURE("中型", Discovery.Type.MEDIUM_CREATURE, arrayOf("T", "C", "F", "B", "H", "G", "A")),
  LARGE_CREATURE("大型", Discovery.Type.LARGE_CREATURE, arrayOf("T", "C", "F", "B", "H", "G", "A")),
  MARINE_LIFE("海洋", Discovery.Type.MARINE_LIFE, arrayOf("V", "C", "G", "B", "I", "H", "A")),
  GEOGRAPHY("地理", Discovery.Type.GEOGRAPHY, arrayOf("U", "C", "G", "B", "I", "H", "A")),
  ASTRONOMY("天文", Discovery.Type.ASTRONOMY, arrayOf("U", "C", "G", "B", "I", "H", "A")),
  WEATHER_PHENOMENA(
    "氣象現象",
    Discovery.Type.WEATHER_PHENOMENA,
    arrayOf("P", "C", "E", "B", "G", "F", "A")
  ),
  FOLKLORE("傳說", Discovery.Type.FOLKLORE, arrayOf("S", "C", "F", "B", "H", "G", "A")),
  FOLKLORE2("傳承", Discovery.Type.FOLKLORE, arrayOf("S", "C", "F", "B", "H", "G", "A")),
  PORT_SETTLEMENT("港口", Discovery.Type.PORT_SETTLEMENT, arrayOf("M", "C", "E", "B", "G", "F", "A")),
  LAND_INVESTIGATION("陸地調查一覽表"),
  LEGACY("遺產一覽表"),
  REPORT("回報");

  companion object {
    private val values = values().associateBy(DiscoverySheet::sheetName)
    fun fromName(name: String): DiscoverySheet? = values[name]
  }
}