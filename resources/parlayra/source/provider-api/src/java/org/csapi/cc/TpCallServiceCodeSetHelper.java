package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpCallServiceCodeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallServiceCodeSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.TpCallServiceCode[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.TpCallServiceCode[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.TpCallServiceCodeSetHelper.id(), "TpCallServiceCodeSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCallServiceCodeHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallServiceCodeSet:1.0";
	}
	public static org.csapi.cc.TpCallServiceCode[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.TpCallServiceCode[] _result;
		int _l_result5 = _in.read_long();
		_result = new org.csapi.cc.TpCallServiceCode[_l_result5];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.TpCallServiceCodeHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.TpCallServiceCode[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.TpCallServiceCodeHelper.write(_out,_s[i]);
		}

	}
}
