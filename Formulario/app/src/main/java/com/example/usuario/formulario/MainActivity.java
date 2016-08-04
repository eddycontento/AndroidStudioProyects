package com.example.usuario.formulario;


import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;


public class MainActivity extends ActionBarActivity implements Validator.ValidationListener {
    EditText cedulaEditText;
    @NotEmpty(message = "Escriba su nombre" )
    EditText nombreEditText;
    @Email(message = "Email incorrecto")
    EditText emailEditText;
    @Password(min = 3, scheme = Password.Scheme.NUMERIC, message = "Password incorrecta")
    EditText passwordEditText;
    @ConfirmPassword(message = "Passwords no coinciden")
    EditText confirmarPasswordEditText;
    @NotEmpty(message = "Escriba su apellido" )
    EditText apellidoEditText;
    //CheckBox aceptarCondicionesCheckBox;
    Validator validator;
    Button verificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cedulaEditText = (EditText) findViewById(R.id.txtcedula);
        nombreEditText = (EditText)findViewById(R.id.txtnombre);
        emailEditText = (EditText)findViewById(R.id.txtcorreo);
        passwordEditText = (EditText)findViewById(R.id.txtcontra);
        confirmarPasswordEditText = (EditText)findViewById(R.id.txtverificarcontra);
        apellidoEditText = (EditText)findViewById(R.id.txtapellido);
        verificar = (Button)findViewById(R.id.btnver);
        //aceptarCondicionesCheckBox = (CheckBox)findViewById(R.id.aceptarCondicionesCheckBox);

        validator = new Validator(this);
        validator.setValidationListener(this);
        /*verificar.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v){
                validator.validate();
            }

        });*/



    }


    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
    }

    /*public void onClick(View v){
        validator.validate();
    }*/



    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Damos de alta los usuarios en nuestra aplicación
    public void alta(View v) {

        if (cedulaEditText.getText().length() != 0){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,

                    "administracion", null, 1);

            SQLiteDatabase bd = admin.getWritableDatabase();

            String cedula = cedulaEditText.getText().toString();
            String nombre = nombreEditText.getText().toString();
            String apellido = apellidoEditText.getText().toString();
            //String ciudad = apellidoEditText.getText().toString();
            String correo = emailEditText.getText().toString();
            String contra = passwordEditText.getText().toString();

            ContentValues registro = new ContentValues();

            registro.put("cedula", cedula);
            registro.put("nombre", nombre);
            registro.put("apellido", apellido);
            registro.put("correo", correo);
            registro.put("contra", contra);

            // los inserto en la base de datos
            bd.insert("usuario", null, registro);

            bd.close();

            // ponemos los campos a vacío para insertar el siguiente usuario
            cedulaEditText.setText(""); nombreEditText.setText(""); apellidoEditText.setText(""); emailEditText.setText("");
            passwordEditText.setText(""); confirmarPasswordEditText.setText("");

            Toast.makeText(this, "Datos del usuario cargados", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "llene todos los campos", Toast.LENGTH_SHORT).show();
            validator.validate();
        }


    }

    // Hacemos búsqueda de usuario por cedula
    public void consulta(View v) {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,

                "administracion", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String cedula = cedulaEditText.getText().toString();

        Cursor fila = bd.rawQuery(

                "select nombre, apellido, correo, contra, verificarcontra from usuario where cedula=" + cedula, null);

        if (fila.moveToFirst()) {

            nombreEditText.setText(fila.getString(0));
            apellidoEditText.setText(fila.getString(1));
            emailEditText.setText(fila.getString(2));
            passwordEditText.setText(fila.getString(3));
            confirmarPasswordEditText.setText(fila.getString(4));

        } else

            Toast.makeText(this, "No existe ningún usuario con ese dni",

                    Toast.LENGTH_SHORT).show();

        bd.close();

    }

}
