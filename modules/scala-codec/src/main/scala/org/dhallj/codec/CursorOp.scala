package org.dhallj.codec

sealed trait CursorOp extends Product with Serializable

final case class DownField(f: String) extends CursorOp
final case class UnionAlternative(f: String) extends CursorOp
