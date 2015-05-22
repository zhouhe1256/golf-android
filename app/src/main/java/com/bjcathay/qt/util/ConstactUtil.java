package com.bjcathay.qt.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.bjcathay.qt.model.BookModel;
import com.igexin.getuiext.data.a;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstactUtil {
    /**
     * 获取所有数据
     *
     * @return
     */
    public static List<BookModel> getAllCallRecords(Context context) {
        List<BookModel> bookModelList = new ArrayList<BookModel>();
        Map<String, String> temp = new HashMap<String, String>();
        Cursor c = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC"
        );
        if (c.moveToFirst()) {
            do {
                BookModel bookModel = new BookModel();
                // 获得联系人的ID号
                String contactId = c.getString(c
                        .getColumnIndex(ContactsContract.Contacts._ID));
                // 获得联系人姓名
                String name = c
                        .getString(c
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // 查看该联系人有多少个电话号码。如果没有这返回值为0
                int phoneCount = c
                        .getInt(c
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String number = null;
                if (phoneCount > 0) {
                    // 获得联系人的电话号码
                    Cursor phones = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null
                    );
                    if (phones.moveToFirst()) {
                        number = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(number);
                String number_ = m.replaceAll("").trim();
                temp.put(name, number_);
                bookModel.setName(name);
                bookModel.setPhone(number_);
                bookModelList.add(bookModel);

            } while (c.moveToNext());
        }
        c.close();
        return bookModelList;
    }

    private static String TAG = "read";

    /*
     * 读取联系人的信息
	 */
    public static List<BookModel> readAllContacts(Context context) {
        List<BookModel> bookModelList = new ArrayList<BookModel>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            BookModel bookModel = new BookModel();
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            Log.i(TAG, contactId);
            Log.i(TAG, name);
            bookModel.setName(name);

			/*
             * 查找该联系人的phone信息
			 */
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                Log.i(TAG, phoneNumber);
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(phoneNumber);
                String number_ = m.replaceAll("").trim();
                if (number_.startsWith("86")) {
                    number_ = number_.substring(1);
                }
                bookModel.setPhone(number_);
            }

			/*
             * 查找该联系人的email信息
			 */
            Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                    null, null);
            int emailIndex = 0;
            if (emails.getCount() > 0) {
                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            }
            while (emails.moveToNext()) {
                String email = emails.getString(emailIndex);
                Log.i(TAG, email);
                bookModel.setData(email);
            }
            bookModelList.add(bookModel);
        }
        cursor.close();

        return bookModelList;
    }
}
