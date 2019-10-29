package com.flys.common_tools.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.io.Serializable;

public class Utils implements Serializable {

    /**
     * apply a font on a menu
     * @param context
     * @param menu
     * @param fontPath path to the font
     */
    public static void applyFontStyleToMenu(Context context, Menu menu, String fontPath) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(context, subMenuItem, fontPath);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(context, menuItem, fontPath);
        }
    }

    /**
     *
     * @param context
     * @param mi
     * @param fontPath
     */
    private static void applyFontToMenuItem(Context context, MenuItem mi, String fontPath) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontPath);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


}
