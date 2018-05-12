package com.android.luttos.autoconsulta;


import org.junit.Assert;
import org.junit.Test;

public class TestPessoa {

    @Test
    public void isIdadeValida1() {
        Pessoa p = new Pessoa("Saulo", 1);
        Boolean resultado = p.isIdadeValida();
        Assert.assertTrue(resultado);
    }

    @Test
    public void isIdadeValida2() {
        Pessoa p = new Pessoa("Saulo", 0);
        Boolean resultado = p.isIdadeValida();
        Assert.assertTrue(resultado);
    }

    @Test
    public void isNotIdadeValida1() {
        Pessoa p = new Pessoa("Saulo", -1);
        Boolean resultado = p.isIdadeValida();
        Assert.assertFalse(resultado);
    }

    @Test
    public void naoPodeVotar1() {
        Pessoa p = new Pessoa("Saulo", 15);
        Boolean resultado = p.podeVotar();
        Assert.assertFalse(resultado);
    }

    @Test
    public void naoPodeVotar2() {
        Pessoa p = new Pessoa("Saulo", -1);
        Boolean resultado = p.podeVotar();
        Assert.assertFalse(resultado);
    }

    @Test
    public void podeVotar1() {
        Pessoa p = new Pessoa("Saulo", 16);
        Boolean resultado = p.podeVotar();
        Assert.assertTrue(resultado);
    }

    @Test
    public void podeVotar2() {
        Pessoa p = new Pessoa("Saulo", 17);
        Boolean resultado = p.podeVotar();
        Assert.assertTrue(resultado);
    }
}

