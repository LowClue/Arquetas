package es.alarcon.arquetanatureble.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;
import es.alarcon.arquetanatureble.R;
import es.alarcon.arquetanatureble.UTIL.Constant;

public class OutsideChamberActivity extends Activity implements View.OnClickListener {

    private static final int NUMBER_PARAMS = 10; //number of parameters

    private static Button bt_next;
    private static RadioGroup RG_params[];
    private static BeanBluetoothDevice beanBluetoothDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_chamber);

        bt_next = (Button) findViewById(R.id.bt_inside);
        bt_next.setOnClickListener(this);

        RG_params       = new RadioGroup[NUMBER_PARAMS];

        //get data from ScanActivity
        beanBluetoothDevice = getIntent().getParcelableExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE);

        RG_params[0]    = (RadioGroup)findViewById(R.id.GrbGrupo1);
        RG_params[1]    = (RadioGroup)findViewById(R.id.GrbGrupo2);
        RG_params[2]    = (RadioGroup)findViewById(R.id.GrbGrupo3);
        RG_params[3]    = (RadioGroup)findViewById(R.id.GrbGrupo4);
        RG_params[4]    = (RadioGroup)findViewById(R.id.GrbGrupo5);
        RG_params[5]    = (RadioGroup)findViewById(R.id.GrbGrupo6);
        RG_params[6]    = (RadioGroup)findViewById(R.id.GrbGrupo7);
        RG_params[7]    = (RadioGroup)findViewById(R.id.GrbGrupo8);
        RG_params[8]    = (RadioGroup)findViewById(R.id.GrbGrupo9);
        RG_params[9]    = (RadioGroup)findViewById(R.id.GrbGrupo10);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_outside_chamber, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //function to control e button
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bt_inside:
                boolean nextActivity = true;
                BeanInformesDB beanInformesDB = new BeanInformesDB();

                //read whole parameters selected.
                int count;
                int IDs[];
                int j = 0;
                int result[];
                int resultSTR;
                for(int i = 0; i < RG_params.length-1 ;i++)
                {
                    j = 0;
                    count = RG_params[i].getChildCount();
                    IDs = new int[count];

                    while(j <= count-1)
                    {
                        IDs[j] = RG_params[i].getChildAt(j).getId();
                        j++;
                    }

                    result = ChekingRadioButton(RG_params[i], IDs[0], IDs[1], IDs[2]);
                    if(result[0] == 0)
                    {
                        nextActivity = false;
                        Toast.makeText(this,"No están todos los check seleccionados",Toast.LENGTH_LONG).show();
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

                    beanInformesDB.gnrlSetExt(resultSTR, i);
                }
                if(nextActivity)
                {
                    Intent intentActivity = new Intent(this, InsideChamberActivity.class);
                    intentActivity.putExtra(Constant.EXTRA_INFORMESDB, beanInformesDB);
                    intentActivity.putExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE,beanBluetoothDevice);
                    this.startActivity(intentActivity);
                }
                break;
            default:
                Log.i(Constant.TAG, "OutsideChamberActivity -- Error not button found.");
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
}
