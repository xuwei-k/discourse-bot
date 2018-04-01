package object discourse_bot {

  @inline def allCatchPrintStackTrace(body: => Any): Unit = {
    try {
      val _ = body
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

}
