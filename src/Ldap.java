import java.util.Enumeration;
import java.util.Hashtable;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class Ldap {

	private static void queryGroup(LdapContext ldapCtx) throws NamingException {
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFilter = "objectClass=organizationalPerson";
		String searchBase = "ou=People,dc=caolong,dc=com";
		String returnedAtts[] = { "distinguishedName", "objectGUID", "name" };
		searchCtls.setReturningAttributes(returnedAtts);
		NamingEnumeration<SearchResult> answer = ldapCtx.search(searchBase,
				searchFilter, searchCtls);
		while (answer.hasMoreElements()) {
			SearchResult sr = answer.next();
			Attributes Attrs = sr.getAttributes();
			if (Attrs != null) {
				NamingEnumeration<?> ne = Attrs.getAll();
				while (ne.hasMore()) {
					Attribute Attr = (Attribute) ne.next();
					String name = Attr.getID();
					Enumeration<?> values = Attr.getAll();
					if (values != null) { // 迭代
						while (values.hasMoreElements()) {
							String value = "";
							if ("objectGUID".equals(name)) {
								value = UUID.nameUUIDFromBytes(
										(byte[]) values.nextElement())
										.toString();
							} else {
								value = (String) values.nextElement();
							}
							System.out.println(name + " " + value);
						}
					}
				}
				System.out.println("=====================");
			}
		}

	}
	public void add(){
		
	}
	public void delete(){}
	public static void main(String[] args){
		
		String url = "ldap://192.168.17.118:389";
		String domain = "dc=caolong,dc=com";
		String user = "cn=root";
		String password = "123456";
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP 工厂
		env.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_PRINCIPAL, user + "," + domain); // 填DN
		env.put(Context.SECURITY_CREDENTIALS, password); // AD Password
		env.put("java.naming.ldap.attributes.binary", "objectSid objectGUID");
		LdapContext ldapCtx = null;
		try {
			ldapCtx = new InitialLdapContext(env, null);
			queryGroup(ldapCtx);
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			if (ldapCtx != null) {
				try {
					ldapCtx.close();
				} catch (NamingException e) {
				}
			}
		}
	}
}
