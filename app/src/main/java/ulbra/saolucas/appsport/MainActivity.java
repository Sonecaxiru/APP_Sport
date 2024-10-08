package ulbra.saolucas.appsport;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtPassos, edtCalorias; // Added EditText for desired calories
    RadioGroup rgOpcoes, rgAtividade;
    TextView textResultado;
    Button btCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Change to your layout name

        edtPassos = findViewById(R.id.edtpassos);
        edtCalorias = findViewById(R.id.edcalorias);
        rgOpcoes = findViewById(R.id.rgopcoes);
        rgAtividade = findViewById(R.id.rgAtividade);
        textResultado = findViewById(R.id.textResultado);
        Button btnCalcular = findViewById(R.id.btcalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularResultado();
            }
        });
    }

    private void calcularResultado() {
        String passosStr = edtPassos.getText().toString();
        String caloriasStr = edtCalorias.getText().toString();
        String caloriasDesejadasStr = edtCalorias.getText().toString(); // Get desired calories

        if (passosStr.isEmpty() || caloriasStr.isEmpty() || caloriasDesejadasStr.isEmpty()) {
            textResultado.setText("Por favor, preencha todos os campos.");
            return;
        }

        int passos = Integer.parseInt(passosStr);
        int calorias = Integer.parseInt(caloriasStr);
        int caloriasDesejadas = Integer.parseInt(caloriasDesejadasStr); // Parse desired calories

        // Obter tamanho do passo selecionado
        double tamanhoPasso = 0;
        int selectedId = rgOpcoes.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButton) { // Longo
            tamanhoPasso = 1; // 1 metro
        } else if (selectedId == R.id.radioButton2) { // Curto
            tamanhoPasso = 0.5; // 0.5 metros
        } else if (selectedId == R.id.radioButton3) { // Médio
            tamanhoPasso = 0.7; // 0.7 metros
        }

        // Calcular distância percorrida em metros
        double distanciaEmMetros = passos * tamanhoPasso; // em metros
        double distanciaEmKilometros = distanciaEmMetros / 1000.0; // Convertendo metros para km
        double distanciaEmMiles = distanciaEmMetros / 1609.34; // Convertendo metros para milhas

        // Calcular calorias queimadas com base na distância
        double caloriasQueimadas;
        double caloriasPorKm;

        // Calorias por km, ajustando de acordo com a atividade
        int selectedAtividadeId = rgAtividade.getCheckedRadioButtonId();
        if (selectedAtividadeId == R.id.rbCaminhando) {
            caloriasPorKm = 50; // Exemplo: 50 calorias por km ao caminhar
        } else {
            caloriasPorKm = 100; // Exemplo: 100 calorias por km ao correr
            // Aumentar a distância em 10% se a opção de correr estiver selecionada
            distanciaEmMetros *= 1.1;
            distanciaEmKilometros = distanciaEmMetros / 1000.0; // Atualiza a conversão para km
            distanciaEmMiles = distanciaEmMetros / 1609.34; // Atualiza a conversão para milhas
        }

        // Calcular calorias queimadas
        caloriasQueimadas = distanciaEmKilometros * caloriasPorKm;

        // Calcular quantas calorias ainda faltam para alcançar o objetivo
        int caloriasFaltando = caloriasDesejadas - (int)caloriasQueimadas;

        // Formatando a saída
        textResultado.setText(String.format(
                "Você queimou %.2f calorias.\nVocê desejava perder %d calorias.\n" +
                        "Você precisa perder mais %d calorias para alcançar seu objetivo.\n" +
                        "Distância percorrida: %.2f m (%.2f km, %.2f milhas).",
                caloriasQueimadas, caloriasDesejadas, Math.max(caloriasFaltando, 0), // Ensure no negative values
                distanciaEmMetros, distanciaEmKilometros, distanciaEmMiles));
    }
}

