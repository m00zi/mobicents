package net.java.slee.resource.diameter.base.events;



import java.util.Iterator;


import net.java.slee.resource.diameter.base.events.avp.AvpList;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.RedirectHostUsageType;

/**
 * Defines an interface representing the Re-Auth-Answer command.
 *
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * <pre>
 * 8.3.2.  Re-Auth-Answer
 * 
 *    The Re-Auth-Answer (RAA), indicated by the Command-Code set to 258
 *    and the message flags' 'R' bit clear, is sent in response to the RAR.
 *    The Result-Code AVP MUST be present, and indicates the disposition of
 *    the request.
 * 
 *    A successful RAA message MUST be followed by an application-specific
 *    authentication and/or authorization message.
 * 
 *    Message Format
 * 
 *       &lt;Re-Auth-Answer&gt;  ::= &lt; Diameter Header: 258, PXY &gt;
 *                  &lt; Session-Id &gt;
 *                  { Result-Code }
 *                  { Origin-Host }
 *                  { Origin-Realm }
 *                  [ User-Name ]
 *                  [ Origin-State-Id ]
 *                  [ Error-Message ]
 *                  [ Error-Reporting-Host ]
 *                * [ Failed-AVP ]
 *                * [ Redirect-Host ]
 *                  [ Redirect-Host-Usage ]
 *                  [ Redirect-Max-Cache-Time ]
 *                * [ Proxy-Info ]
 *                * [ AVP ]
 * </pre>
 */
public interface ReAuthAnswer extends DiameterMessage {

    int commandCode = 258;

    /**
     * Returns true if the Session-Id AVP is present in the message.
     */
    boolean hasSessionId();

    /**
     * Returns the value of the Session-Id AVP, of type UTF8String.
     * @return the value of the Session-Id AVP or null if it has not been set on this message
     */
    String getSessionId();

    /**
     * Sets the value of the Session-Id AVP, of type UTF8String.
     * @throws IllegalStateException if setSessionId has already been called
     */
    void setSessionId(String sessionId);

    /**
     * Returns true if the Result-Code AVP is present in the message.
     */
    boolean hasResultCode();

    /**
     * Returns the value of the Result-Code AVP, of type Unsigned32.
     * Use {@link #hasResultCode()} to check the existence of this AVP.  
     * @return the value of the Result-Code AVP
     * @throws IllegalStateException if the Result-Code AVP has not been set on this message
     */
    long getResultCode();

    /**
     * Sets the value of the Result-Code AVP, of type Unsigned32.
     * @throws IllegalStateException if setResultCode has already been called
     */
    void setResultCode(long resultCode);

    /**
     * Returns true if the Origin-Host AVP is present in the message.
     */
    boolean hasOriginHost();

    /**
     * Returns the value of the Origin-Host AVP, of type DiameterIdentity.
     * @return the value of the Origin-Host AVP or null if it has not been set on this message
     */
    DiameterIdentityAvp getOriginHost();

    /**
     * Sets the value of the Origin-Host AVP, of type DiameterIdentity.
     * @throws IllegalStateException if setOriginHost has already been called
     */
    void setOriginHost(DiameterIdentityAvp originHost);

    /**
     * Returns true if the Origin-Realm AVP is present in the message.
     */
    boolean hasOriginRealm();

    /**
     * Returns the value of the Origin-Realm AVP, of type DiameterIdentity.
     * @return the value of the Origin-Realm AVP or null if it has not been set on this message
     */
    DiameterIdentityAvp getOriginRealm();

    /**
     * Sets the value of the Origin-Realm AVP, of type DiameterIdentity.
     * @throws IllegalStateException if setOriginRealm has already been called
     */
    void setOriginRealm(DiameterIdentityAvp originRealm);

    /**
     * Returns true if the User-Name AVP is present in the message.
     */
    boolean hasUserName();

    /**
     * Returns the value of the User-Name AVP, of type UTF8String.
     * @return the value of the User-Name AVP or null if it has not been set on this message
     */
    String getUserName();

    /**
     * Sets the value of the User-Name AVP, of type UTF8String.
     * @throws IllegalStateException if setUserName has already been called
     */
    void setUserName(String userName);

    /**
     * Returns true if the Origin-State-Id AVP is present in the message.
     */
    boolean hasOriginStateId();

    /**
     * Returns the value of the Origin-State-Id AVP, of type Unsigned32.
     * Use {@link #hasOriginStateId()} to check the existence of this AVP.  
     * @return the value of the Origin-State-Id AVP
     * @throws IllegalStateException if the Origin-State-Id AVP has not been set on this message
     */
    long getOriginStateId();

    /**
     * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
     * @throws IllegalStateException if setOriginStateId has already been called
     */
    void setOriginStateId(long originStateId);

    /**
     * Returns true if the Error-Message AVP is present in the message.
     */
    boolean hasErrorMessage();

    /**
     * Returns the value of the Error-Message AVP, of type UTF8String.
     * @return the value of the Error-Message AVP or null if it has not been set on this message
     */
    String getErrorMessage();

    /**
     * Sets the value of the Error-Message AVP, of type UTF8String.
     * @throws IllegalStateException if setErrorMessage has already been called
     */
    void setErrorMessage(String errorMessage);

    /**
     * Returns true if the Error-Reporting-Host AVP is present in the message.
     */
    boolean hasErrorReportingHost();

    /**
     * Returns the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
     * @return the value of the Error-Reporting-Host AVP or null if it has not been set on this message
     */
    DiameterIdentityAvp getErrorReportingHost();

    /**
     * Sets the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
     * @throws IllegalStateException if setErrorReportingHost has already been called
     */
    void setErrorReportingHost(DiameterIdentityAvp errorReportingHost);

    /**
     * Returns the set of Failed-AVP AVPs. The returned array contains
     * the AVPs in the order they appear in the message.
     * A return value of null implies that no Failed-AVP AVPs have been set.
     * The elements in the given array are FailedAvp objects.
     */
    FailedAvp[] getFailedAvps();

    /**
     * Sets a single Failed-AVP AVP in the message, of type Grouped.
     * @throws IllegalStateException if setFailedAvp or setFailedAvps
     *  has already been called
     */
    void setFailedAvp(FailedAvp failedAvp);

    /**
     * Sets the set of Failed-AVP AVPs, with all the values in the given array.
     * The AVPs will be added to message in the order in which they appear in the array.
     *
     * Note: the array must not be altered by the caller following this call, and
     * getFailedAvps() is not guaranteed to return the same array instance,
     * e.g. an "==" check would fail.
     *
     * @throws IllegalStateException if setFailedAvp or setFailedAvps
     *  has already been called
     */
    void setFailedAvps(FailedAvp[] failedAvps);

    /**
     * Returns the set of Redirect-Host AVPs. The returned array contains
     * the AVPs in the order they appear in the message.
     * A return value of null implies that no Redirect-Host AVPs have been set.
     * The elements in the given array are DiameterURI objects.
     */
    DiameterURI[] getRedirectHosts();

    /**
     * Sets a single Redirect-Host AVP in the message, of type DiameterURI.
     * @throws IllegalStateException if setRedirectHost or setRedirectHosts
     *  has already been called
     */
    void setRedirectHost(DiameterURI redirectHost);

    /**
     * Sets the set of Redirect-Host AVPs, with all the values in the given array.
     * The AVPs will be added to message in the order in which they appear in the array.
     *
     * Note: the array must not be altered by the caller following this call, and
     * getRedirectHosts() is not guaranteed to return the same array instance,
     * e.g. an "==" check would fail.
     *
     * @throws IllegalStateException if setRedirectHost or setRedirectHosts
     *  has already been called
     */
    void setRedirectHosts(DiameterURI[] redirectHosts);

    /**
     * Returns true if the Redirect-Host-Usage AVP is present in the message.
     */
    boolean hasRedirectHostUsage();

    /**
     * Returns the value of the Redirect-Host-Usage AVP, of type Enumerated.
     * @return the value of the Redirect-Host-Usage AVP or null if it has not been set on this message
     */
    RedirectHostUsageType getRedirectHostUsage();

    /**
     * Sets the value of the Redirect-Host-Usage AVP, of type Enumerated.
     * @throws IllegalStateException if setRedirectHostUsage has already been called
     */
    void setRedirectHostUsage(RedirectHostUsageType redirectHostUsage);

    /**
     * Returns true if the Redirect-Max-Cache-Time AVP is present in the message.
     */
    boolean hasRedirectMaxCacheTime();

    /**
     * Returns the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
     * Use {@link #hasRedirectMaxCacheTime()} to check the existence of this AVP.  
     * @return the value of the Redirect-Max-Cache-Time AVP
     * @throws IllegalStateException if the Redirect-Max-Cache-Time AVP has not been set on this message
     */
    long getRedirectMaxCacheTime();

    /**
     * Sets the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
     * @throws IllegalStateException if setRedirectMaxCacheTime has already been called
     */
    void setRedirectMaxCacheTime(long redirectMaxCacheTime);

    /**
     * Returns the set of Proxy-Info AVPs. The returned array contains
     * the AVPs in the order they appear in the message.
     * A return value of null implies that no Proxy-Info AVPs have been set.
     * The elements in the given array are ProxyInfo objects.
     */
    ProxyInfoAvp[] getProxyInfos();

    /**
     * Sets a single Proxy-Info AVP in the message, of type Grouped.
     * @throws IllegalStateException if setProxyInfo or setProxyInfos
     *  has already been called
     */
    void setProxyInfo(ProxyInfoAvp proxyInfo);

    /**
     * Sets the set of Proxy-Info AVPs, with all the values in the given array.
     * The AVPs will be added to message in the order in which they appear in the array.
     *
     * Note: the array must not be altered by the caller following this call, and
     * getProxyInfos() is not guaranteed to return the same array instance,
     * e.g. an "==" check would fail.
     *
     * @throws IllegalStateException if setProxyInfo or setProxyInfos
     *  has already been called
     */
    void setProxyInfos(ProxyInfoAvp[] proxyInfos);

    /**
     * Returns the set of extension AVPs. The returned array contains the extension AVPs
     * in the order they appear in the message.
     * A return value of null implies that no extensions AVPs have been set.
     */
    AvpList getExtensionAvps();

    /**
     * Sets the set of extension AVPs with all the values in the given array.
     * The AVPs will be added to message in the order in which they appear in the array.
     *
     * Note: the array must not be altered by the caller following this call, and
     * getExtensionAvps() is not guaranteed to return the same array instance,
     * e.g. an "==" check would fail.
     *
     * @throws AvpNotAllowedException if an AVP is encountered of a type already known to this class
     *   (i.e. an AVP for which get/set methods already appear in this class)
     * @throws IllegalStateException if setExtensionAvps has already been called
     */
    void setExtensionAvps(AvpList avps) throws AvpNotAllowedException;

}
