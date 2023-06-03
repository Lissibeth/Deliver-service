package com.example.myapplication;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainPresenter
{
    private List<Order> orderList;

    private List<Order> selectedOrders;

    public Deliverer loggedDeliverer;

    IDatabase model;

    public MainPresenter(Deliverer deliverer, IDatabase database )
    {
        model = database;
        selectedOrders = new ArrayList<>();
        Login(deliverer);
    }

    public void Login(Deliverer deliverer)
    {
        loggedDeliverer = deliverer;
        UpdateOrders();
    }
    public void  DeselectAll()
    {
        selectedOrders.clear();
    }

    public void Sort()
    {
        var sortedList = orderList.stream().sorted().collect(Collectors.toList());
        if(sortedList.equals(orderList))
        {
            Collections.reverse(orderList);
            return;
        }
        orderList = sortedList;
    }
    public void Accept()
    {
        orderList.removeAll(selectedOrders);
        loggedDeliverer.AcceptOrders(selectedOrders);
    }
    public boolean IsSelected(Order order)
    {
        return selectedOrders.contains(order);
    }

    private void UpdateOrders()
    {
        orderList = model.GetOrders();
    }
    public List<Order> GetOrders()
    {
        return orderList;
    }
    public void SelectedOrder(Order order) throws Exception {
        if(!order.CanDeliver(loggedDeliverer))
            throw new Exception("Невозможно доставить заказ");
        selectedOrders.add(order);
    }
    public void DeselectOrder(Order order)
    {
        selectedOrders.remove(order);
    }

    public int GetPriceOfSelected()
    {
        return selectedOrders.stream().mapToInt(Order::GetPrice).sum();
    }
}
