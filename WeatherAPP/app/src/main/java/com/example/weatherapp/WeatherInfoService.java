package com.example.weatherapp;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
// Служба для считывания информации о погоде

public class WeatherInfoService extends Service {
    IBinder mBinder;      // Интерфейс связи с сервисом погоды

    @Override
    public void onCreate() {
        // Служба создается
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Служба стартовала
        return START_REDELIVER_INTENT;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // подключение к сервису погоды и получение данных
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // отключение от сервиса погоды
        return true;
    }

    @Override
    public void onDestroy() {
        // Уничтожение службы
    }


}
