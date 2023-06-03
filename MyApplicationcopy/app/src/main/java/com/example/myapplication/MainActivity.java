package com.example.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.FragmentManager;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Deliverer deliverer = new Deliverer("Мега имя курьера", false,true);
        presenter = new MainPresenter(deliverer, new RandomDatabase());


        ListView listView = UpdateList();

        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Order order = (Order) listView.getAdapter().getItem(position);
            if(presenter.IsSelected(order))
            {
                presenter.DeselectOrder(order);
                arg1.setBackgroundColor(Color.WHITE);
                return;
            }
            try {
                    presenter.SelectedOrder(order);
                    arg1.setBackgroundColor(Color.GREEN);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateDeliverer(Deliverer d)
    {
        ((TextView)findViewById(R.id.nameDel)).setText("Имя: "+ d.getName());
        ((TextView)findViewById(R.id.ordersCount)).setText("Количество заказов: "+ d.getAcceptedOrders().size());
        ((TextView)findViewById(R.id.totalIncomes)).setText("Всего доходов: "+ d.GetTotalCost());
    }

    public void SortPriceClick(View view)
    {
        presenter.Sort();
        UpdateList();
    }

    public void DenyClick(View view)
    {
        for(int index = 0; index < ((ViewGroup) findViewById(R.id.list)).getChildCount(); index++) {
            View nextChild = ((ViewGroup) findViewById(R.id.list)).getChildAt(index);
            nextChild.setBackgroundColor(Color.WHITE);
        }
        presenter.DeselectAll();
    }

    public ListView PriceClick(View view)
    {
        List<Order> orderList = presenter.GetOrders();
        Order[] arr = orderList.toArray(new Order[1]);
        OrderAdapter orderAdapter = new OrderAdapter(this, arr);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(orderAdapter);
        return listView;
    }

    public void AcceptClick(View view)
    {

        Toast.makeText(getApplicationContext(), "Взяты заказы на сумму: " + presenter.GetPriceOfSelected(), Toast.LENGTH_SHORT).show();
        presenter.Accept();
        UpdateList();
        UpdateDeliverer(presenter.loggedDeliverer);
    }

    private ListView UpdateList() {
        List<Order> orderList = presenter.GetOrders();
        Order[] arr = orderList.toArray(new Order[1]);
        OrderAdapter orderAdapter = new OrderAdapter(this, arr);

        UpdateDeliverer(presenter.loggedDeliverer);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(orderAdapter);
        return listView;
    }

}
