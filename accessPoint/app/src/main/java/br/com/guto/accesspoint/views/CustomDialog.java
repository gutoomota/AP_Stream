package br.com.guto.accesspoint.views;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import br.com.guto.accesspoint.R;


public class CustomDialog
{
    private static ProgressDialog progressDialog = null;
    //private static AlertDialog alertDialog = null;

    public static void createProgressDialog(Context context)
    {
        if (progressDialog == null || !progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(context, R.style.CustomDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public static void setMessageProgressDialog(String message)
    {
        if (progressDialog != null) progressDialog.setMessage(message);
    }

    public static void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    /*public static void createAlertDialog(Context context, String text)
    {
        if (alertDialog == null || !alertDialog.isShowing())
        {
            alertDialog = new AlertDialog.Builder(context, R.style.CustomDialogStyle).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setMessage(text);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();

            Typeface Montserrat = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat_Regular.otf");
            TextView message = (TextView) alertDialog.findViewById(android.R.id.message);
            message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            message.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            message.setTypeface(Montserrat);
            //message.setGravity(Gravity.CENTER);
            TextView button = (TextView) alertDialog.findViewById(android.R.id.button1);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            button.setTypeface(Montserrat);
        }
    }

    public static void createNotification(Context context, String title, String text)
    {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(text);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cloud));
        builder.setContentIntent(intent);

        Notification notification = builder.build();
        //notification.defaults |= Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.PRIORITY_HIGH;
        manager.notify(0, notification);
    }*/
}