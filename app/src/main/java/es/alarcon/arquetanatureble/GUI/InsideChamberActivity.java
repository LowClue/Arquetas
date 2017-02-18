package es.alarcon.arquetanatureble.GUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;
import es.alarcon.arquetanatureble.R;
import es.alarcon.arquetanatureble.UTIL.Constant;

public class InsideChamberActivity extends Activity implements View.OnClickListener {


    private static final int NUMBER_PARAMS = 5;
    private static BeanInformesDB beanInformesDB;
    private static BeanBluetoothDevice beanBluetoothDevice;

    private static Bitmap bmp;
    private static ImageView img;
    private static RadioGroup RG_params[];
    private static TextView tV_comment;

    public static Button bt_nextDevice;
    public static Button bt_photo;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_chamber);

        //get information about informes
        beanInformesDB      = getIntent().getParcelableExtra(Constant.EXTRA_INFORMESDB);
        beanBluetoothDevice = getIntent().getParcelableExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE);

        RG_params       = new RadioGroup[NUMBER_PARAMS];
        RG_params[0]    = (RadioGroup)findViewById(R.id.GrbGrupoEI1);
        RG_params[1]    = (RadioGroup)findViewById(R.id.GrbGrupoEI2);
        RG_params[2]    = (RadioGroup)findViewById(R.id.GrbGrupoEI3);
        RG_params[3]    = (RadioGroup)findViewById(R.id.GrbGrupoEI4);
        RG_params[4]    = (RadioGroup)findViewById(R.id.GrbGrupoEI5);

        img             = (ImageView) findViewById(R.id.IV_camera);

        tV_comment      = (TextView) findViewById(R.id.tB_comentario);

        bt_nextDevice  = (Button) findViewById(R.id.bt_siguienteDevice);
        bt_photo        = (Button) findViewById(R.id.bt_camera);

        bt_nextDevice.setOnClickListener(this);
        bt_photo.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inside_chamber, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //func to control diferents button actions
    @Override
    public void onClick(View view)
    {
        Intent intentActivity;
        switch (view.getId())
        {
            case R.id.bt_siguienteDevice:
                //read whole parameters selected.
                boolean nextActivity = true;
                int count;
                int IDs[];
                int j = 0;
                int result[];
                int resultSTR;
                for(int i = 0; i < RG_params.length-1 ;i++)
                {
                    count = RG_params[i].getChildCount();
                    IDs = new int[count];

                    j = 0;
                    while(j <= count -1)
                    {
                        IDs[j] = RG_params[i].getChildAt(j).getId();
                        j++;
                    }

                    result = ChekingRadioButton(RG_params[i], IDs[0], IDs[1], IDs[2]);
                    if(result[0] == 0)
                    {
                        nextActivity = false;
                        Toast.makeText(this, "No están todos los check seleccionados", Toast.LENGTH_LONG).show();
                        break;
                    }

                    switch (result[1])
                    {
                        case 1:
                            resultSTR = beanInformesDB.BUENO;
                            break;
                        case 2:
                            resultSTR = beanInformesDB.ACEPTABLE;
                            break;
                        case 3:
                            resultSTR = beanInformesDB.NECESITA_REPARACION;
                            break;
                        default:
                            resultSTR = 0;
                            break;
                    }

                    beanInformesDB.gnrlSetEi(resultSTR,i);

                }if(nextActivity)
                {
                    //add comment and photo.
                    if(bmp != null)
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] photoData = stream.toByteArray();

                        beanInformesDB.setFoto(photoData);
                    }
                    beanInformesDB.setComentario(tV_comment.getText().toString());

                    intentActivity= new Intent(this, DeviceActivity.class);
                    intentActivity.putExtra(Constant.EXTRA_INFORMESDB, beanInformesDB);
                    intentActivity.putExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE,beanBluetoothDevice);
                    this.startActivity(intentActivity);
                }
                break;

            case R.id.bt_camera:
                intentActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentActivity, 0);
                break;

            default:
                Log.i(Constant.TAG, "InsideChamberActivity -- Error not button found.");
        }
    }

    //checker radioButton active
    private int[] ChekingRadioButton(RadioGroup radioGroup, int IDButton1, int IDButton2, int IDButton3)
    {
        int out[] = new int[2];
        out[0] = 0;
        out[1] = 0;
        int radioButtonSelected  = radioGroup.getCheckedRadioButtonId();

        if(IDButton1 == radioButtonSelected)
        {
            out[0] = 1;
            out[1] = 1;//bueno
        }
        else if(IDButton2 == radioButtonSelected)
        {
            out[0] = 1;
            out[1] = 2;//aceptable
        }
        else if(IDButton3 == radioButtonSelected)
        {
            out[0] = 1;
            out[1] = 3;//necesita reparacion
        }

        return out;
    }

    // this method gives us the result of picture taken with tha camera.
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        Log.i(Constant.TAG, "InsideChamberActivity -- onActivityResult: taking the image wich has been taking with the camera");
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            Bundle ext = data.getExtras();
            bmp        = (Bitmap)ext.get("data");
            img.setImageBitmap(bmp);
        }
        else
        {
            Log.i(Constant.TAG,"InsideChamberActivity -- onActivityResult: It couldn't possible take the image");
        }
    }
}
