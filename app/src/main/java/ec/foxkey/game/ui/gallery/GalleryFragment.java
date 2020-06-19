package ec.foxkey.game.ui.gallery;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ec.foxkey.game.R;

public class GalleryFragment extends Fragment {

    ImageView img1, img2, img3, img4, img5, img6, imgCapturada;
    Switch sw1, sw2;
    int level = 12;
    int control, imageId, contador = 0;
    int[] images = new int[]{
            R.drawable.anml_0,
            R.drawable.anml_1,
            R.drawable.anml_2,
            R.drawable.anml_3,
            R.drawable.anml_4,
            R.drawable.anml_5,
            R.drawable.anml_6,
            R.drawable.anml_7,
            R.drawable.anml_8,
            R.drawable.anml_9,
            R.drawable.anml_10,
            R.drawable.anml_11};

    Bitmap bmp1 = null, bmp2 = null, resultBmp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        img1 = (ImageView) root.findViewById(R.id.imageView1);
        img2 = (ImageView) root.findViewById(R.id.imageView2);
        img3 = (ImageView) root.findViewById(R.id.imageView3);
        img4 = (ImageView) root.findViewById(R.id.imageView4);
        img5 = (ImageView) root.findViewById(R.id.imageView5);
        img6 = (ImageView) root.findViewById(R.id.imageView6);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img1, R.id.imageView1, v);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img2, R.id.imageView2, v);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img3, R.id.imageView3, v);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img4, R.id.imageView4, v);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img5, R.id.imageView5, v);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare(img6, R.id.imageView6, v);
            }
        });
        sw1 = (Switch) root.findViewById(R.id.sw_fcimg);
        sw2 = (Switch) root.findViewById(R.id.sw_dfimg);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw2.setChecked(false);
                    level = 12;
                    actualizar();
                } else {
                    sw2.setChecked(true);
                    level = 6;
                    actualizar();
                }
            }
        });
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw1.setChecked(false);
                    level = 6;
                    actualizar();
                } else {
                    sw1.setChecked(true);
                    level = 12;
                    actualizar();
                }
            }
        });

        FloatingActionButton newImageButton = (FloatingActionButton) root.findViewById(R.id.newGame);
        newImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actualizar();
            }
        });
        newImageButton.performClick();

        return root;
    }

    public void compare(final ImageView imgBox, int idBox, final View v) {
        if (bmp1 == null) {//primera vez
            bmp1 = ((BitmapDrawable) imgBox.getDrawable()).getBitmap();
            control = idBox;
            imgCapturada = (ImageView) v.findViewById(control);
            imgCapturada.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {//segunda vez
            bmp2 = ((BitmapDrawable) imgBox.getDrawable()).getBitmap();
            imgBox.setBackgroundColor(getResources().getColor(R.color.gray));
            if (bmp1 == bmp2) {//imagenes iguales
                imgCapturada.setEnabled(false);
                imgBox.setEnabled(false);
                bmp1 = null;
                bmp2 = null;
                contador++;
                if (contador == 3) {
                    confirmar();
                }
            } else {//imagenes diferentes
                imgBox.setBackgroundColor(Color.TRANSPARENT);
                imgCapturada.setBackgroundColor(Color.TRANSPARENT);
                bmp1 = null;
                bmp2 = null;
                Toast.makeText(getContext(), "Las imágenes no son iguales!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void actualizar() {

        img1.setEnabled(true);
        img2.setEnabled(true);
        img3.setEnabled(true);
        img4.setEnabled(true);
        img5.setEnabled(true);
        img6.setEnabled(true);

        img1.setBackgroundColor(Color.TRANSPARENT);
        img2.setBackgroundColor(Color.TRANSPARENT);
        img3.setBackgroundColor(Color.TRANSPARENT);
        img4.setBackgroundColor(Color.TRANSPARENT);
        img5.setBackgroundColor(Color.TRANSPARENT);
        img6.setBackgroundColor(Color.TRANSPARENT);

        imageId = (int) (Math.random() * images.length);

        img1.setImageResource(images[imageId]);
        img1.setImageAlpha(level);
        img6.setImageResource(images[imageId]);

        imageId = (int) (Math.random() * images.length);

        img2.setImageResource(images[imageId]);
        img2.setImageAlpha(level);
        img4.setImageResource(images[imageId]);

        imageId = (int) (Math.random() * images.length);

        img3.setImageResource(images[imageId]);
        img3.setImageAlpha(level);
        img5.setImageResource(images[imageId]);

        contador = 0;
    }

    public void confirmar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Bien Hecho, Juego terminado");
        builder.setMessage("¿Deseas continuar jugando?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                actualizar();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Juego terminado!", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
