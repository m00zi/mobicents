package org.mobicents.slee.resource.parlay.session;

import org.csapi.IpInterface;
import org.csapi.IpService;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.TpCommonExceptions;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

/**
 *
 * Class Description for IpServiceStub
 */
public class IpServiceStub implements IpService {

    /* (non-Javadoc)
     * @see org.csapi.IpServiceOperations#setCallback(org.csapi.IpInterface)
     */
    public void setCallback(IpInterface arg0) throws TpCommonExceptions,
            P_INVALID_INTERFACE_TYPE {
        //  Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.csapi.IpServiceOperations#setCallbackWithSessionID(org.csapi.IpInterface, int)
     */
    public void setCallbackWithSessionID(IpInterface arg0, int arg1)
            throws TpCommonExceptions, P_INVALID_SESSION_ID,
            P_INVALID_INTERFACE_TYPE {
        //  Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_release()
     */
    public void _release() {
        //  Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_non_existent()
     */
    public boolean _non_existent() {
        //  Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_hash(int)
     */
    public int _hash(int maximum) {
        //  Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_is_a(java.lang.String)
     */
    public boolean _is_a(String repositoryIdentifier) {
        //  Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_get_domain_managers()
     */
    public DomainManager[] _get_domain_managers() {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_duplicate()
     */
    public Object _duplicate() {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_get_interface_def()
     */
    public Object _get_interface_def() {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_is_equivalent(org.omg.CORBA.Object)
     */
    public boolean _is_equivalent(Object other) {
        //  Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_get_policy(int)
     */
    public Policy _get_policy(int policy_type) {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_request(java.lang.String)
     */
    public Request _request(String operation) {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_set_policy_override(org.omg.CORBA.Policy[], org.omg.CORBA.SetOverrideType)
     */
    public Object _set_policy_override(Policy[] policies,
            SetOverrideType set_add) {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_create_request(org.omg.CORBA.Context, java.lang.String, org.omg.CORBA.NVList, org.omg.CORBA.NamedValue)
     */
    public Request _create_request(Context ctx, String operation,
            NVList arg_list, NamedValue result) {
        //  Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.omg.CORBA.Object#_create_request(org.omg.CORBA.Context, java.lang.String, org.omg.CORBA.NVList, org.omg.CORBA.NamedValue, org.omg.CORBA.ExceptionList, org.omg.CORBA.ContextList)
     */
    public Request _create_request(Context ctx, String operation,
            NVList arg_list, NamedValue result, ExceptionList exclist,
            ContextList ctxlist) {
        //  Auto-generated method stub
        return null;
    }

}
