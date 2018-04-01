package discourse_bot

final class RingBuffer[A: reflect.ClassTag](maxSize: Int) extends Seq[A] {
  private[this] val array = new Array[A](maxSize)
  private[this] var read = 0
  private[this] var write = 0
  private[this] var count_ = 0

  def length = count_
  override def size = count_

  def apply(i: Int): A = {
    if (i >= count_) throw new IndexOutOfBoundsException(i.toString)
    else array((read + i) % maxSize)
  }

  def +=(elem: A): this.type = {
    array(write) = elem
    write = (write + 1) % maxSize
    if (count_ == maxSize) read = (read + 1) % maxSize
    else count_ += 1

    this
  }

  def ++=(iter: Iterable[A]): this.type = {
    for (elem <- iter) this += elem
    this
  }

  override def iterator: Iterator[A] {
    def next: A

    def hasNext: Boolean

    var idx: Int
  } = new Iterator[A] {
    var idx = 0
    def hasNext = idx != count_
    def next = {
      val res = apply(idx)
      idx += 1
      res
    }
  }

}
