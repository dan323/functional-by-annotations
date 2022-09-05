package com.dan323.functional;

import com.dan323.functional.data.Identity;
import com.dan323.functional.data.IdentityMonad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentityMonadTest {

    @Test
    public void identityPure() {
        Identity<Integer> identity = IdentityMonad.pure(5);
        assertEquals(5, Identity.runIdentity(identity));
    }

    @Test
    public void identityMap() {
        Identity<Integer> identity = IdentityMonad.map(IdentityMonad.pure(5), x -> x * x);
        assertEquals(25, Identity.runIdentity(identity));
    }

    @Test
    public void identityFapply() {
        Identity<Integer> identity = IdentityMonad.fapply(IdentityMonad.pure(5), IdentityMonad.pure(x -> x * x));
        assertEquals(25, Identity.runIdentity(identity));
    }


    @Test
    public void identityJoin() {
        Identity<Integer> identity = IdentityMonad.join(IdentityMonad.pure(IdentityMonad.pure(9)));
        assertEquals(9, Identity.runIdentity(identity));
    }


    @Test
    public void identityLiftA2() {
        Identity<Integer> identity = IdentityMonad.liftA2((x, y) -> x + 5 * y, IdentityMonad.pure(5), IdentityMonad.pure(1));
        assertEquals(10, Identity.runIdentity(identity));
    }
}
