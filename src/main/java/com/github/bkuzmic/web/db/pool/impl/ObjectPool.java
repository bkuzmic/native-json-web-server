package com.github.bkuzmic.web.db.pool.impl;

import com.github.bkuzmic.web.db.pool.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Object pool taken from
 * <a>https://sourcemaking.com/design_patterns/object_pool/java</a>
 *
 * @param <T> any object that requires pooling
 */
public abstract class ObjectPool<T> implements Pool<T> {
    private final long expirationTime;

    private final Map<T, Long> locked;
    private final Map<T, Long> unlocked;

    public ObjectPool(long expirationTime) {
        this.expirationTime = expirationTime;
        this.locked = new HashMap<>();
        this.unlocked = new HashMap<>();
    }

    protected abstract T create();

    protected abstract boolean validate(T o);

    protected abstract void expire(T o);

    @Override
    public synchronized T checkOut() {
        long now = System.currentTimeMillis();
        if (unlocked.size() > 0) {
            Set<T> objectSet = unlocked.keySet();
            for (T t : objectSet) {
                if ((now - unlocked.get(t)) > expirationTime) {
                    // object has expired
                    unlocked.remove(t);
                    expire(t);
                } else {
                    if (validate(t)) {
                        unlocked.remove(t);
                        locked.put(t, now);
                        return (t);
                    } else {
                        // object failed validation
                        unlocked.remove(t);
                        expire(t);
                    }
                }
            }
        }
        // no objects available, create a new one
        T t = create();
        locked.put(t, now);
        return (t);
    }

    @Override
    public synchronized void checkIn(T t) {
        locked.remove(t);
        unlocked.put(t, System.currentTimeMillis());
    }

    @Override
    public int size() {
        return locked.size() + unlocked.size();
    }

    @Override
    public void destroy() {
        Set<T> unLockedObjectSet = unlocked.keySet();
        for (T t : unLockedObjectSet) {
            unlocked.remove(t);
            expire(t);
        }
        Set<T> lockedObjectSet = locked.keySet();
        for (T t : lockedObjectSet) {
            unlocked.remove(t);
            expire(t);
        }
    }
}
