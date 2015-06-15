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
    private final static String[] mContactsProjection = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
    };

    public static List<BookModel> getQuickRecords(Context context) {
        List<BookModel> bookModelList = new ArrayList<BookModel>();
        Cursor phoneCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null,
                null, null);
        if (0 < phoneCursor.getCount()) {
            phoneCursor.moveToFirst();
            while (phoneCursor.getPosition() != phoneCursor.getCount()) {
                BookModel bookModel = new BookModel();
                String phoneNumber = phoneCursor.getString(2)
                        .replace("-", "");
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(phoneNumber);
                String number_ = m.replaceAll("").trim();
                if (number_.startsWith("86")) {
                    number_ = number_.substring(2);
                } else if (number_.startsWith("010")) {
                    number_ = number_.substring(3);
                }
                bookModel.setPhone(number_);
                String name = phoneCursor.getString(3).replace("-", "");
                bookModel.setName(phoneCursor.getString(3).replace("-", ""));
                Log.i("phone:name:", number_ + ":" + name);
                phoneCursor.moveToNext();
                if (ValidformUtil.isMobileNo(number_))
                    bookModelList.add(bookModel);
            }
        }
        phoneCursor.close();
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
