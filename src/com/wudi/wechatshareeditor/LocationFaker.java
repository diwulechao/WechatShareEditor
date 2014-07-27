package com.wudi.wechatshareeditor;

import java.lang.reflect.Method;

import android.text.TextUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class LocationFaker implements IXposedHookLoadPackage {

	private static SettingsHelper settingsHelper = new SettingsHelper();

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.tencent.mm"))
			return;

		Class<?> locationClass = null;
		try {
			locationClass = XposedHelpers.findClass(
					"com.tencent.map.location.s", lpparam.classLoader);
		} catch (XposedHelpers.ClassNotFoundError e) {
			XposedBridge.log("can not found the location class");
			return;
		}

		Method[] ms = XposedHelpers.findMethodsByExactParameters(locationClass,
				void.class, String.class);
		if (ms != null) {
			for (Method method : ms) {
				XposedHelpers.findAndHookMethod(locationClass,
						method.getName(), String.class, new XC_MethodHook() {
							@Override
							protected void beforeHookedMethod(
									MethodHookParam param) throws Throwable {
								// validate the input
								String input = (String) param.args[0];
								if (!TextUtils.isEmpty(input)
										&& input.contains("location")) {
									settingsHelper.reload();

									try {
										double la = Double
												.parseDouble(settingsHelper
														.getString("latitude",
																"-10001"));
										double lo = Double
												.parseDouble(settingsHelper
														.getString("longitude",
																"-10001"));

										if (la > -10000 && lo > -10000) {
											param.args[0] = "{\"location\":{ \"latitude\":"
													+ String.valueOf(la)
													+ ",\"longitude\":"
													+ String.valueOf(lo)
													+ ",\"altitude\":0,\"accuracy\":6  },\"bearing\":\"47,18\"}";
										}
									} catch (Exception e) {
										XposedBridge.log("parse double fail");
									}
								}

							}
						});
			}
		}

	}
}
