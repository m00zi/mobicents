package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMADEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMADEventDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMADEventDataHelper.id(),"TpPAMADEventData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AgentName", org.csapi.pam.TpPAMFQNameListHelper.type(), null),new org.omg.CORBA.StructMember("AgentType", org.csapi.TpStringListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMADEventData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMADEventData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMADEventData:1.0";
	}
	public static org.csapi.pam.TpPAMADEventData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMADEventData result = new org.csapi.pam.TpPAMADEventData();
		result.AgentName = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		result.AgentType = org.csapi.TpStringListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMADEventData s)
	{
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.AgentName);
		org.csapi.TpStringListHelper.write(out,s.AgentType);
	}
}
