package io.github.aal337.banana2abstractart.color

enum class Lightness(val colorChange: ColorChange) {
    LIGHT(ColorChange(0xC0, 0xC0, 0xC0)),
    NORMAL(ColorChange(0, 0, 0)),
    DARK(-ColorChange(63, 63, 63))
}

val Lightness.next: Lightness
    get() = when (this) {
        Lightness.LIGHT -> NORMAL
        NORMAL -> DARK
        DARK -> LIGHT
    }

enum class Hue(val normalColor: Color) {
    RED(Color(0xFF, 0, 0)),
    YELLOW(Color(0xFF, 0xFF, 0)),
    GREEN(Color(0, 0xFF, 0)),
    CYAN(Color(0, 0xFF, 0xFF)),
    BLUE(Color(0, 0, 0xFF)),
    MAGENTA(Color(0xFF, 0, 0xFF))
}

val Hue.next: Hue
    get() = when (this) {
        RED -> YELLOW
        YELLOW -> GREEN
        GREEN -> CYAN
        CYAN -> BLUE
        BLUE -> MAGENTA
        MAGENTA -> RED
    }

operator fun Hue.plus(lightness: Lightness) = normalColor + lightness.colorChange

fun Lightness.next(steps: Int): Lightness {
    var result = this
    repeat (steps) {
        result = result.next
    }
    return result
}

fun Hue.next(steps: Int): Hue {
    var result = this
    repeat (steps) {
        result = result.next
    }
    return result
}

class ColorComponent(val value: Int) {
    init {
        require(value in 0..0xFF)
    }
}

class ColorChangeComponent(val value: Int) {
    init {
        require(value in -0xFF..0xFF)
    }
}

operator fun ColorChangeComponent.unaryMinus() = ColorChangeComponent(-value)

fun Int.toColorComponent() = ColorComponent(when (this) {
    in 0..0xFF -> this
    else if this < 0 -> 0
    else -> 0xFF
})

fun Int.toColorChangeComponent() = ColorChangeComponent(when (this) {
    in -0xFF..0xFF -> this
    else if this < -0xFF -> -0xFF
    else -> 0xFF
})

operator fun ColorComponent.plus(change: ColorChangeComponent) = (value + change.value).toColorComponent()

data class Color(val red: ColorComponent, val green: ColorComponent, val blue: ColorComponent) {
    constructor(red: Int, green: Int, blue: Int) : this(
        red.toColorComponent(),
        green.toColorComponent(),
        blue.toColorComponent()
    )
}

data class ColorChange(val red: ColorChangeComponent, val green: ColorChangeComponent, val blue: ColorChangeComponent) {
    constructor(red: Int, green: Int, blue: Int) : this(
        red.toColorChangeComponent(),
        green.toColorChangeComponent(),
        blue.toColorChangeComponent()
    )
}

operator fun ColorChange.unaryMinus() = ColorChange(-red, -green, -blue)

operator fun Color.plus(other: ColorChange) = Color(red + other.red, green + other.green, blue + other.blue)