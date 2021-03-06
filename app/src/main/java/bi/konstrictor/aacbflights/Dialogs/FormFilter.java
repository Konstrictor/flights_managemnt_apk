package bi.konstrictor.aacbflights.Dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import bi.konstrictor.aacbflights.Filterable;
import bi.konstrictor.aacbflights.Fragments.FragmentPassager;
import bi.konstrictor.aacbflights.Fragments.FragmentVol;
import bi.konstrictor.aacbflights.Host;
import bi.konstrictor.aacbflights.MainActivity;
import bi.konstrictor.aacbflights.Models.Aeroport;
import bi.konstrictor.aacbflights.Models.Avion;
import bi.konstrictor.aacbflights.Models.Compagnie;
import bi.konstrictor.aacbflights.Models.Vol;
import bi.konstrictor.aacbflights.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormFilter extends Dialog {
    private final Filterable filterable;
    private final Boolean only_date;
    Spinner spinner_compagnie;
    DatePicker picker_date_debut, picker_date_fin;
    private Button btn_cancel, btn_submit;
    private MainActivity context;

    public FormFilter(MainActivity context, final Filterable filterable, Boolean only_date) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.form_filtre);
        this.context = context;
        this.filterable = filterable;
        this.only_date = only_date;

        spinner_compagnie = findViewById(R.id.spinner_compagnie);
        picker_date_debut = findViewById(R.id.picker_date_debut);
        picker_date_fin = findViewById(R.id.picker_date_fin);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);

        if (only_date) spinner_compagnie.setVisibility(View.GONE);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterable.cancelFiltering();
                dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFiltering();
                dismiss();
            }
        });
        fillSpinner();
    }
    private void performFiltering() {
        Date debut = getDateFromPicker(picker_date_debut);
        Date fin = getDateFromPicker(picker_date_fin);
        if(only_date){
            filterable.performFiltering(debut, fin, null);
            return;
        }
        Compagnie compagnie = (Compagnie) spinner_compagnie.getSelectedItem();
        filterable.performFiltering(debut, fin, compagnie);
    }
    private void fillSpinner() {
        ArrayAdapter adapter_compagnie = new ArrayAdapter(
                context,
                R.layout.support_simple_spinner_dropdown_item,
                context.compagnies);
        spinner_compagnie.setAdapter(adapter_compagnie);
    }

    private Date getDateFromPicker(DatePicker dp ) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        return calendar.getTime();
    }
}