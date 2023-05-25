package constants

sealed class Constants {
    abstract val message: String
    object ViewChanged: Constants(){
        override val message: String
            get() = "viewChanged"
    }
    object Result: Constants() {
        override val message: String
            get() = "result"
    }

    object Operation: Constants(){
        override val message: String
            get() = "operation"
    }
    object Number1: Constants(){
        override val message: String
            get() = "number1"
    }
    object Number2: Constants(){
        override val message: String
            get() = "number2"
    }
}