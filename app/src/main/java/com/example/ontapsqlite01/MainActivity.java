package com.example.ontapsqlite01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edtName,edtPrice,edtQuantity;
    private AppCompatButton btnSave;
    private ModelAdapter modelAdapter;
    private List<Model> modelList;
    private Database database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initView
        edtName = findViewById(R.id.Name);
        edtPrice = findViewById(R.id.Price);
        edtQuantity = findViewById(R.id.Quantity);
        btnSave = findViewById(R.id.btnSaveData);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //init Database
        database = new Database(MainActivity.this);
        modelAdapter = new ModelAdapter(this, database.getAllModel(), new IClickListener() {
            @Override
            public void onUpdate(Model model) {
                    showUpdateDialog(model);

            }

            @Override
            public void onDelete(Model model) {
               showDialogDelete(model);

            }
        });
        recyclerView.setAdapter(modelAdapter);
        //event for button

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get infor from editext
                String Name = edtName.getText().toString().trim();
                String Price = edtPrice.getText().toString().trim();
                String Quantity = edtQuantity.getText().toString().trim();
                if(Name.isEmpty() || Price.isEmpty() || Quantity.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fiel does not empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double _Price = Double.parseDouble(Price);
                    int _Quantity = Integer.parseInt(Quantity);
                    Model model = new Model();
                    model.setName(Name);
                    model.setPrice(_Price);
                    model.setQuantity(_Quantity);
                    database.addModel(model);
                    modelAdapter.updateList(database.getAllModel());
                    modelAdapter.notifyDataSetChanged();
                    edtName.setText("");
                    edtPrice.setText("");
                    edtQuantity.setText("");
                }

            }
        });

    }

    private void showDialogDelete(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa item");
        builder.setMessage("Bạn có chắc chắn muốn xóa ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteModel(model.getName());
                modelAdapter.updateList(database.getAllModel());
                modelAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUpdateDialog(Model model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập Nhật Thông Tin");
        View view = getLayoutInflater().inflate(R.layout.dialog_update,null);
        builder.setView(view);
        final  EditText edtUpdateName = view.findViewById(R.id.edtUpdateName);
        final EditText edtUpdatePrice = view.findViewById(R.id.edtUpdatePrice);
        final EditText edtUpdateQuantity = view.findViewById(R.id.edtUpdateQuantity);
        //Set origin value for dialog
        edtUpdateName.setText(model.getName());
        edtUpdatePrice.setText(String.valueOf(model.getPrice()));
        edtUpdateQuantity.setText(String.valueOf(model.getQuantity()));
        builder.setPositiveButton("Cập Nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = edtUpdateName.getText().toString().trim();
                double newPrice = Double.parseDouble(edtUpdatePrice.getText().toString().trim());
                int newQuantity = Integer.parseInt(edtUpdateQuantity.getText().toString().trim());
                //update information into database
                model.setName(newName);
                model.setPrice(newPrice);
                model.setQuantity(newQuantity);
                database.updateModel(model);
                //show display
               modelAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}