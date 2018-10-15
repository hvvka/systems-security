package com.blogspot.debukkitsblog.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Datapackage contains the data transmitted and received by
 * SimpleServerClient Clients and Servers
 * 
 * @author Leonard Bienbeck
 * @version 2.4.1
 */
public class Datapackage extends ArrayList<Object> {
	
	private String senderID = "UNSIGNED";
	private String senderGroupName = "UNSIGNED";

	/**
	 * Constructs a Datapackage consisting of an ID used by the remote application
	 * to identify the package and payload data
	 * 
	 * @param id
	 *            The ID used by the remote application to identify the package
	 * @param o
	 *            The payload
	 */
	public Datapackage(String id, Object... o) {
		this.add(0, id);
        Collections.addAll(this, o);
	}

	/**
	 * Returns the ID of the package. The Datapackage can be identified with this.
	 * 
	 * @return The ID of the package
	 */
	public String id() {
		if (!(this.get(0) instanceof String)) {
			throw new IllegalArgumentException("Identifier of Datapackage is not a String");
		}
		return (String) this.get(0);
	}

	/**
	 * Returns the ID of the sender of this Datapackage. This makes answering
	 * easier.
	 * 
	 * @return The ID of the sender of this Datapackage or UNSIGNED if the
	 *         Datapackage was not signed by the sender
	 */
	public String getSenderID() {
		return this.senderID;
	}

	/**
	 * Returns the name of the group the sender of this Datapackage is member of.
	 * This makes answering easier.
	 * 
	 * @return The name of the group the sender of this Datapackage is member of or
	 *         UNSIGNED if the Datapackage was not signed by the sender
	 */
	public String getSenderGroup() {
		return this.senderGroupName;
	}

	/**
	 * Signs the Datapackage with the given ID. This method should only be called
	 * internally by the sender. Do not call this method unless you know what you
	 * do.<br>
	 * With this information, the receiver will know the source of every incoming
	 * Datapackage. This is useful to send responses if you don't want to use
	 * sendReply.
	 * 
	 * @param senderID
	 *            The ID of the sender
	 * @param senderGroup
	 *            The name of the group the sender is member of
	 */
    void sign(String senderID, String senderGroup) {
		this.senderID = senderID;
		this.senderGroupName = senderGroup;
	}

	/**
	 * Returns the Datapackage as ArrayList, containing the Datapackage's ID at
	 * index 0 and the payload from index 1 to the end. This method is redundant
	 * since Datapackage extends ArrayList.
	 * 
	 * @return The Datapackage itself.
	 */
	public List<Object> open() {
		return this;
	}

}