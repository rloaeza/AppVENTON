package com.mas_aplicaciones.appventon;

import android.widget.EditText;

import java.util.StringTokenizer;

public class evaluacion_de_views
{
    public evaluacion_de_views()
    {

    }

    public boolean es_numero(String editText, int edadminima)
    {
        try {

            if(Integer.parseInt(editText)>edadminima && Integer.parseInt(editText)<100)
            {
                return  true;
            }
            else
            {
                return false;
            }
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }

    }
    public  boolean contrasena_correcta(String contrasena)
    {
        if(contrasena.length()>=8)
        {
            if(contrasena.contains("0")||contrasena.contains("1")||contrasena.contains("2")||contrasena.contains("3")||contrasena.contains("4")||
               contrasena.contains("5")||contrasena.contains("6")||contrasena.contains("7")||contrasena.contains("8")||contrasena.contains("9"))
            {
                if(contrasena_contiene_mayuscula(contrasena))
                {

                    return true;

                }
                return false;
            }
            return false;
        }
        return false;
    }
    private  boolean contrasena_contiene_mayuscula(String contrasena)
    {

        for (char elemento: contrasena.toCharArray())
        {
            if(elemento>='A' && elemento<='Z')
            {
                return true;
            }
        }
        return false;
    }
    public boolean emailValidado(String email)
    {
        if(email.endsWith("@hotmail.com") || email.endsWith("@hotmail.es") || email.endsWith("@outlook.com") || email.endsWith("@outlook.es") || email.endsWith("@gmail.com") || email.endsWith("@yahoo.com") || email.endsWith("@yahoo.es"))
        {
            if(email_con_un_arroba(email))
            {
                return true;
            }
            return false;
        }
        return false;
    }
    private  boolean email_con_un_arroba(String email)
    {
        StringTokenizer  st = new StringTokenizer(email,"@");
        if( st.countTokens()==2)
        {
            return true;
        }
        return false;


    }

}
