package com.segment.analytics.android.integrations.webengage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.segment.analytics.Analytics;
import com.segment.analytics.Traits;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.AliasPayload;
import com.segment.analytics.integrations.GroupPayload;
import com.segment.analytics.integrations.IdentifyPayload;
import com.segment.analytics.integrations.Integration;
import com.segment.analytics.integrations.ScreenPayload;
import com.segment.analytics.integrations.TrackPayload;
import com.segment.analytics.internal.Utils;
import com.webengage.sdk.android.Channel;
import com.webengage.sdk.android.UserProfile;
import com.webengage.sdk.android.WebEngage;
import com.webengage.sdk.android.utils.Gender;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class WebEngageIntegration extends Integration<WebEngage> {

    Context context = null;

    private static final String KEY = "WebEngage";
    private static final String BIRTHDAY_KEY = "birthday";
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String GENDER_KEY = "gender";
    private static final String LAST_NAME_KEY = "lastName";
    private static final String PHONE_KEY = "phone";
    private static final String EMAIL_KEY = "email";
    private static final String INDUSTRY_KEY = "industry";
    private static final String NAME_KEY = "name";
    private static final String ADDRESS_KEY = "address";


    private static final String HASHED_EMAIL_KEY = "we_hashed_email";
    private static final String HASHED_PHONE_KEY = "we_hashed_phone";
    private static final String PUSH_OPT_IN_KEY = "we_push_opt_in";
    private static final String SMS_OPT_IN_KEY = "we_sms_opt_in";
    private static final String EMAIL_OPT_IN_KEY = "we_email_opt_in";


    public static final Factory FACTORY = new Factory() {
        @Override
        public Integration<?> create(ValueMap settings, Analytics analytics) {
            WebEngage.engage(analytics.getApplication().getApplicationContext());
            return new WebEngageIntegration(analytics.getApplication().getApplicationContext());
        }

        @Override
        public String key() {
            return KEY;
        }
    };

    public WebEngageIntegration(Context context) {
        this.context = context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        super.onActivityStarted(activity);
        WebEngage.get().analytics().start(activity);

    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        super.onActivityStopped(activity);
        WebEngage.get().analytics().stop(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        super.onActivitySaveInstanceState(activity, outState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
    }

    @Override
    public void identify(IdentifyPayload identify) {
        super.identify(identify);
        if (identify != null) {
            if (identify.userId() != null) {
                WebEngage.get().user().login(identify.userId());
            }

            Map<String, Object> traits = null;
            if (identify.traits() != null) {
                traits = new HashMap<String, Object>(identify.traits());
            }

            UserProfile.Builder userProfileBuilder = newUserProfileBuilder();
            if (traits != null) {
                if (traits.get(BIRTHDAY_KEY) != null) {
                    Date birthDate = null;
                    try {
                        birthDate = Utils.parseISO8601Date((String) traits.get(BIRTHDAY_KEY));
                    } catch (Exception e) {

                    }
                    if (birthDate != null) {
                        Calendar gregorianCalendar = null;
                        gregorianCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
                        gregorianCalendar.setTime(birthDate);
                        userProfileBuilder.setBirthDate(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH) + 1, gregorianCalendar.get(Calendar.DAY_OF_MONTH));
                        traits.remove(BIRTHDAY_KEY);
                    }
                }

                if (traits.get(FIRST_NAME_KEY) == null && traits.get(LAST_NAME_KEY) == null) {
                    String name = (String) traits.get(NAME_KEY);
                    if (name != null) {
                        String[] components = name.split(" ");
                        userProfileBuilder.setFirstName(components[0]);
                        if (components.length > 1) {
                            userProfileBuilder.setLastName(components[components.length - 1]);
                        }
                        traits.remove(NAME_KEY);
                    }
                }
                if (traits.get(FIRST_NAME_KEY) != null) {
                    userProfileBuilder.setFirstName((String) traits.get(FIRST_NAME_KEY));
                    traits.remove(FIRST_NAME_KEY);
                }
                if (traits.get(LAST_NAME_KEY) != null) {
                    userProfileBuilder.setLastName((String) traits.get(LAST_NAME_KEY));
                    traits.remove(LAST_NAME_KEY);
                }
                if (traits.get(INDUSTRY_KEY) != null) {
                    userProfileBuilder.setCompany((String) traits.get(INDUSTRY_KEY));
                    traits.remove(INDUSTRY_KEY);
                }
                if (traits.get(EMAIL_KEY) != null) {
                    userProfileBuilder.setEmail((String) traits.get(EMAIL_KEY));
                    traits.remove(EMAIL_KEY);
                }
                if (traits.get(GENDER_KEY) != null) {
                    userProfileBuilder.setGender(Gender.valueByString((String) traits.get(GENDER_KEY)));
                    traits.remove(GENDER_KEY);
                }
                if (traits.get(PHONE_KEY) != null) {
                    userProfileBuilder.setPhoneNumber((String) traits.get(PHONE_KEY));
                    traits.remove(PHONE_KEY);
                }

                if (traits.get(HASHED_EMAIL_KEY) != null) {
                    userProfileBuilder.setHashedEmail((String) traits.get(HASHED_EMAIL_KEY));
                    traits.remove(HASHED_EMAIL_KEY);
                }

                if (traits.get(HASHED_PHONE_KEY) != null) {
                    userProfileBuilder.setHashedPhoneNumber((String) traits.get(HASHED_PHONE_KEY));
                    traits.remove(HASHED_PHONE_KEY);
                }

                if (traits.get(PUSH_OPT_IN_KEY) != null) {
                    userProfileBuilder.setOptIn(Channel.PUSH, Boolean.valueOf(String.valueOf(traits.get(PUSH_OPT_IN_KEY))));
                    traits.remove(PUSH_OPT_IN_KEY);
                }

                if (traits.get(SMS_OPT_IN_KEY) != null) {
                    userProfileBuilder.setOptIn(Channel.SMS, Boolean.valueOf(String.valueOf(traits.get(SMS_OPT_IN_KEY))));
                    traits.remove(SMS_OPT_IN_KEY);
                }

                if (traits.get(EMAIL_OPT_IN_KEY) != null) {
                    userProfileBuilder.setOptIn(Channel.EMAIL, Boolean.valueOf(String.valueOf(traits.get(EMAIL_OPT_IN_KEY))));
                    traits.remove(EMAIL_OPT_IN_KEY);
                }

                if (traits.get(ADDRESS_KEY) != null) {
                    Traits.Address address = (Traits.Address) traits.get(ADDRESS_KEY);
                    traits.remove(ADDRESS_KEY);
                    traits.putAll(address);
                }

                WebEngage.get().user().setUserProfile(userProfileBuilder.build());
                WebEngage.get().user().setAttributes(traits);
            }
        }
    }

    @Override
    public void group(GroupPayload group) {
        super.group(group);
    }

    @Override
    public void track(TrackPayload track) {
        super.track(track);
        if (track.event() != null) {
            WebEngage.get().analytics().track(track.event(), track.properties());
        }
    }

    @Override
    public void alias(AliasPayload alias) {
        super.alias(alias);
    }

    @Override
    public void screen(ScreenPayload screen) {
        super.screen(screen);
        if (screen.name() != null) {
            Map<String, Object> properties = new HashMap<String, Object>();
            if (screen.properties() != null) {
                properties.putAll(screen.properties());
            }
            if (screen.category() != null) {
                properties.put("category", screen.category());
            }
            WebEngage.get().analytics().screenNavigated(screen.name(), properties);
        }
    }

    @Override
    public void reset() {
        super.reset();
        WebEngage.get().user().logout();
    }



    @Override
    public WebEngage getUnderlyingInstance() {
        return (WebEngage) WebEngage.get();
    }

    public UserProfile.Builder newUserProfileBuilder() {
        return new UserProfile.Builder();
    }
}
