package api;

/**
* api/DataNodeHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��12��28�� ������ ����06ʱ39��12�� GMT+08:00
*/

public final class DataNodeHolder implements org.omg.CORBA.portable.Streamable
{
  public api.DataNode value = null;

  public DataNodeHolder ()
  {
  }

  public DataNodeHolder (api.DataNode initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.DataNodeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.DataNodeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.DataNodeHelper.type ();
  }

}