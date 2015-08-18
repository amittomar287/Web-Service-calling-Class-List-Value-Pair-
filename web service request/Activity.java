//Way for calling webservice:

List<NameValuePair> param = new ArrayList<NameValuePair>();
                            param.add(new BasicNameValuePair("method","userlogin"));
                            param.add(new BasicNameValuePair("username",strUserName));
                            param.add(new BasicNameValuePair("gplusid",MDMConstants.GPLUS_ID));
                            param.add(new BasicNameValuePair("password",strUserPass));
                            param.add(new BasicNameValuePair("email",MDMConstants.EMAIL_ID));
                            param.add(new BasicNameValuePair("schoolcode",strSchoolCode));

new WebRequestTask(LoginActivity.this, param, _handler,
                                    WebServiceDetails.PID_LOGIN,true).execute();





// Define the Handler that receives messages from wep api call.
    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if(response_code!=0){
                String strResponse=  (String) msg.obj;
                Log.v("Response", strResponse);
                if(strResponse!=null && strResponse.length()>0){
                    switch (response_code) {
                    case WebServiceDetails.PID_LOGIN:
                    {
                        try {
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            String strSuccess =jsonResponse.optString("success");
                            if(strSuccess.equalsIgnoreCase("true")){
                                String strUserId =jsonResponse.optString("userid");
                                MDMConstants.USER_ID=strUserId;

                                Intent intent=new Intent(LoginActivity.this,DashBoardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                String strError =jsonResponse.optString("error");
                                Toast.makeText(LoginActivity.this, strError, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case WebServiceDetails.PID_FORGOT_PASSWORD:
                    {
                        try {
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            String strSuccess =jsonResponse.optString("success");
                            if(strSuccess.equalsIgnoreCase("true")){
                                String strResult =jsonResponse.optString("result");
                                Toast.makeText(LoginActivity.this, strResult, Toast.LENGTH_LONG).show();
                            }else{
                                String strError =jsonResponse.optString("error");
                                Toast.makeText(LoginActivity.this, strError, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case WebServiceDetails.PID_SCHOOL_CODE_FINDER:
                    {
                        try {
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            String strSuccess =jsonResponse.optString("success");
                            if(strSuccess.equalsIgnoreCase("true")){
                                String strResult =jsonResponse.optString("result");
                                Toast.makeText(LoginActivity.this, "The school code is sent to your email. Please take a look", Toast.LENGTH_LONG).show();
                            }else{
                                String strError =jsonResponse.optString("error");
                                Toast.makeText(LoginActivity.this, "No records found, please check the information.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    default:
                        break;
                    }
                }else{
                    Toast.makeText(LoginActivity.this, MDMConstants.SERVER_ERROR, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(LoginActivity.this, MDMConstants.SERVER_ERROR, Toast.LENGTH_LONG).show();
            }
        }
    };
