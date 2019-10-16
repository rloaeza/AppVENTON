package com.mas_aplicaciones.appventon.firebase;

import java.util.StringTokenizer;

public class evaluacion_de_views
{
    public evaluacion_de_views()
    {

    }

    // Max length = 3 XML

    public boolean es_numero(String editText, int edadminima)
    {
        try {

            return Integer.parseInt(editText) > edadminima && Integer.parseInt(editText) < 100;
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

                    return contrasena_contiene_mayuscula(contrasena);



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
            return email_con_un_arroba(email);
        }
        return false;
    }
    public boolean telefonoValido(String tel)
    {
            boolean ban = true;
            try
            {
                //si falla la conversion, tirará exception.
                Long.parseLong(tel);
                //max lengt = 10 en XML
                if(tel.length()!=10)
                {
                    ban =  false;
                }
            /*    else
                {
                    ban = false;
                }
            */

            }
            catch (NumberFormatException ex)
            {

                  ban = false;
            }

        return ban;
    }
    private  boolean email_con_un_arroba(String email)
    {
        StringTokenizer  st = new StringTokenizer(email,"@");
        return st.countTokens() == 2;


    }

}
