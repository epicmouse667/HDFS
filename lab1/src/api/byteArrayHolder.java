package api;


/**
* api/byteArrayHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年12月28日 星期四 下午06时39分12秒 GMT+08:00
*/

public final class byteArrayHolder implements org.omg.CORBA.portable.Streamable
{
  public byte value[] = null;

  public byteArrayHolder ()
  {
  }

  public byteArrayHolder (byte[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.byteArrayHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.byteArrayHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.byteArrayHelper.type ();
  }

}
