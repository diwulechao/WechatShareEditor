package com.wudi.wechatshareeditor;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class WechatShareEditor implements IXposedHookLoadPackage {

	private static SettingsHelper settingsHelper = new SettingsHelper();

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

		if (!lpparam.packageName.equals("com.tencent.mm"))
			return;

		XposedHelpers.findAndHookMethod("com.tencent.mm.ui.MMActivity",
				lpparam.classLoader, "onCreate", Bundle.class,
				new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						Activity a = (Activity) param.thisObject;

						// XposedBridge.log(a.getClass().getPackage().getName()
						// + ',' + a.getClass().getSimpleName());
						settingsHelper.reload();
						String newStr;
						String oldStr = a.getIntent().getStringExtra(
								"Ksnsupload_title");
						if (!TextUtils.isEmpty(oldStr)) {
							newStr = settingsHelper.getString("title", "");
							if (!TextUtils.isEmpty(newStr)) {
								a.getIntent().putExtra("Ksnsupload_title",
										newStr);
							}
						}

						oldStr = a.getIntent().getStringExtra(
								"Ksnsupload_imgurl");
						if (!TextUtils.isEmpty(oldStr)) {
							newStr = settingsHelper.getString("image_url", "");
							if (!TextUtils.isEmpty(newStr)) {
								a.getIntent().putExtra("Ksnsupload_imgurl",
										newStr);
							}
						}

						oldStr = a.getIntent()
								.getStringExtra("src_displayname");
						if (!TextUtils.isEmpty(oldStr)) {
							newStr = settingsHelper.getString(
									"src_displayname", "");
							if (!TextUtils.isEmpty(newStr)) {
								a.getIntent().putExtra("src_displayname",
										newStr);
							}
						}

						oldStr = a.getIntent().getStringExtra("src_username");
						if (!TextUtils.isEmpty(oldStr)) {

							newStr = settingsHelper.getString("src_username",
									"");
							if (!TextUtils.isEmpty(newStr)) {
								a.getIntent().putExtra("src_username", newStr);
							}
						}

						oldStr = a.getIntent()
								.getStringExtra("Ksnsupload_link");
						if (!TextUtils.isEmpty(oldStr)) {
							newStr = settingsHelper
									.getString("upload_link", "");
							if (!TextUtils.isEmpty(newStr)) {
								a.getIntent().putExtra("Ksnsupload_link",
										newStr);
							}
						}

						int newInt;
						int oldInt = a.getIntent().getIntExtra(
								"Ksnsupload_height", -1);
						if (oldInt != -1) {
							newStr = settingsHelper.getString("upload_height",
									"-1");
							if (!TextUtils.isEmpty(newStr)) {
								newInt = Integer.parseInt(newStr);
								if (newInt != -1) {
									a.getIntent().putExtra("Ksnsupload_height",
											newInt);
								}
							}
						}

						oldInt = a.getIntent().getIntExtra("Ksnsupload_source",
								0);
						if (oldInt != 0) {
							newStr = settingsHelper.getString("upload_source",
									"0");
							if (!TextUtils.isEmpty(newStr)) {
								newInt = Integer.parseInt(newStr);
								if (newInt != 0) {
									a.getIntent().putExtra("Ksnsupload_source",
											newInt);
								}
							}
						}

						oldInt = a.getIntent().getIntExtra("Ksnsupload_width",
								-1);
						if (oldInt != -1) {
							newStr = settingsHelper.getString("upload_width",
									"-1");
							if (!TextUtils.isEmpty(newStr)) {
								newInt = Integer.parseInt(newStr);
								if (newInt != -1) {
									a.getIntent().putExtra("Ksnsupload_width",
											newInt);
								}
							}
						}

					}
				});

	}

}
