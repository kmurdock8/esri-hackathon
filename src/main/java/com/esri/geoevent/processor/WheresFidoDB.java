package com.esri.geoevent.processor;

import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class WheresFidoDB {
    private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(WheresFidoProcessor.class);

    public List<User> getUsersWithNotificationsOn() {
        List<User> usersOut = new ArrayList<>();
       User kelsey = new User("kmurdock@esri.com", 34.056861, -117.195332);
       User zack = new User("zack.m.allen@gmail.com", 34.0633, -117.209903);
       User demo = new User("wheresfidodemo@gmail.com", 34.071693, -117.191686);

       usersOut.add(kelsey);
       usersOut.add(zack);
       usersOut.add(demo);

       return usersOut;
    }

    public void addToWatching(int searchID, User u) {
        u.addToWatchingList(searchID);
    }

    public boolean checkIfUserIsWatching(int searchID, User u) {
        List<Integer> watching = u.getWatchingList();
        if (watching.contains(searchID)) {
            return true;
        } else {
            return false;
        }
    }
}
