package net.zdsoft.framework.utils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperConnector implements Watcher {

    private static ZookeeperConnector zookeeper;
    private static CountDownLatch countDown = new CountDownLatch(1);
    private static boolean state = false;

    private ZookeeperConnector(String ip) {
        this.ip = ip;
    }

    private String ip;

    public static ZookeeperConnector newInstance(String ip) {
        if (zookeeper == null) {
            zookeeper = new ZookeeperConnector(ip);
        }
        return zookeeper;
    }

    public static void main(String[] args) {
        ZookeeperConnector zookeeper = new ZookeeperConnector("192.168.0.155:2181");
        System.out.println(zookeeper.check());

    }

    public boolean check() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(ip, 2000, this);
            getCountDown().await();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if(zooKeeper != null)
                try {
                    zooKeeper.close();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return state;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            getCountDown().countDown();
            state = true;
        }
    }

    public static CountDownLatch getCountDown() {
        return countDown;
    }
}
