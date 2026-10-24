import re

first_aid_en = [
    "Check for injuries on yourself and those around you before moving. Do not move seriously injured individuals unless they are in immediate danger.",
    "Apply direct pressure to bleeding wounds using a clean cloth or bandage. Elevate the injured area if possible to reduce blood flow.",
    "For suspected fractures, do not attempt to realign the bone. Immobilize the injured area using a splint or padding.",
    "If someone is trapped under debris, do not attempt to move heavy items alone. Call out for help and try to create breathing space without destabilizing the structure.",
    "Provide CPR if the person is unresponsive and not breathing normally. Ensure the airway is clear before beginning chest compressions.",
    "Clean any wounds immediately with clean, uncontaminated water and soap. Floodwaters carry hazardous bacteria and chemicals that can cause severe infections.",
    "Treat for shock by keeping the affected person warm and lying down. Elevate their legs slightly if there are no spinal injuries.",
    "Keep wounds covered with sterile bandages. If redness, swelling, or excessive pain occurs, seek medical attention as soon as possible.",
    "Avoid consuming food or water that has come into contact with floodwater. Dehydration is common, so ensure you drink only bottled or boiled water.",
    "Watch for signs of hypothermia, such as shivering, confusion, and slurred speech. Warm the person slowly using dry blankets and warm, non-alcoholic liquids.",
    "Treat cuts and lacerations caused by flying glass immediately. Clean with antiseptic and apply a tight, sterile dressing.",
    "Immobilize any suspected fractures caused by falling objects. Use rigid materials like cardboard or magazines as temporary splints.",
    "If an object is impaled in the body, DO NOT remove it. Pack bulky dressings around the object to stabilize it and control bleeding until professional help arrives.",
    "Monitor those trapped or injured for signs of crush syndrome, which can occur after prolonged compression of muscle tissue.",
    "Keep emergency pathways clear of hazardous debris to ensure safe evacuation once the storm has fully passed.",
    "For heat exhaustion (heavy sweating, weakness, cold/clammy skin, nausea): move the person to a cooler environment immediately and have them sip water slowly.",
    "Apply cool, wet cloths to the body or take a cool bath. Loosen clothing and fan the person to lower their body temperature.",
    "For heat stroke (high body temperature, hot/red/dry skin, confusion, loss of consciousness): call for medical help IMMEDIATELY. This is a life-threatening emergency.",
    "While waiting for help, move the heat stroke victim to the shade and rapidly cool them using cold water or ice packs placed on the neck, armpits, and groin.",
    "Do not give fluids to a person suffering from heat stroke if they are unconscious or vomiting, as they may choke.",
    "Assess for crush injuries and severe trauma. Do not attempt to move someone trapped by heavy rocks or mud unless their airway is blocked.",
    "Clear dirt and debris from the victim's mouth and nose to ensure they can breathe.",
    "Control any active bleeding with direct pressure, but avoid applying pressure to suspected broken bones in the ribcage or skull.",
    "Keep the victim calm and still to prevent further injury, especially if a spinal injury is suspected from the impact.",
    "Signal for help by shouting, whistling, or flashing a light so search and rescue teams can locate you in the debris.",
    "Rescue individuals only if it is safe for you to do so. Use ropes or flotation devices rather than entering the hazardous, debris-filled water.",
    "Perform CPR immediately if a rescued victim is unresponsive and has inhaled water. Ensure the airway is clear of mud or sand.",
    "Treat hypothermia by removing wet clothing and wrapping the victim in dry blankets. Huddle together to share body heat if necessary.",
    "Apply pressure to lacerations caused by floating debris. Infection is highly likely, so clean wounds thoroughly with bottled water.",
    "Provide psychological first aid by keeping survivors calm and reassuring them, as the trauma of a tsunami can induce severe shock.",
    "Call for medical help immediately. Lightning strike victims do not carry an electrical charge and are safe to touch and treat.",
    "Check for breathing and a heartbeat. Lightning often causes cardiac arrest. Begin CPR immediately if the victim is unresponsive and pulseless.",
    "Assess for and treat severe burns where the lightning entered and exited the body.",
    "Monitor the victim for signs of shock, confusion, or temporary paralysis, which are common after a strike.",
    "Keep the victim warm and immobilize them if you suspect they have suffered fractures or spinal injuries from being thrown by the blast."
]

first_aid_hi = [
    "हिलने से पहले अपनी और दूसरों की चोटों की जांच करें। गंभीर रूप से घायल को न हिलाएं।",
    "रक्तस्राव वाले घावों पर साफ कपड़े से सीधा दबाव डालें।",
    "फ्रैक्चर होने पर हड्डी को सीधा करने का प्रयास न करें।",
    "मलबे में फंसे होने पर अकेले भारी चीजें न हटाएं।",
    "यदि व्यक्ति सांस नहीं ले रहा है तो सीपीआर (CPR) दें।",
    "किसी भी घाव को तुरंत साफ पानी और साबुन से धोएं।",
    "प्रभावित व्यक्ति को गर्म रखकर और लिटाकर सदमे का इलाज करें।",
    "घावों को बाँझ पट्टियों से ढके रखें।",
    "बाढ़ के पानी के संपर्क में आने वाले भोजन या पानी का सेवन करने से बचें।",
    "हाइपोथर्मिया के लक्षणों पर नज़र रखें और व्यक्ति को धीरे-धीरे गर्म करें।",
    "उड़ते हुए कांच से होने वाले कट का तुरंत इलाज करें।",
    "गिरने वाली वस्तुओं के कारण होने वाले किसी भी संदिग्ध फ्रैक्चर को स्थिर करें।",
    "यदि कोई वस्तु शरीर में चुभ गई है, तो उसे न निकालें।",
    "क्रश सिंड्रोम के संकेतों के लिए फंसे या घायल लोगों की निगरानी करें।",
    "तूफान गुजरने के बाद सुरक्षित निकासी के लिए आपातकालीन मार्गों को साफ रखें।",
    "गर्मी से थकावट होने पर व्यक्ति को तुरंत ठंडे स्थान पर ले जाएं और धीरे-धीरे पानी पिलाएं।",
    "शरीर पर ठंडे, गीले कपड़े लगाएं या ठंडे पानी से नहलाएं।",
    "हीट स्ट्रोक होने पर तुरंत चिकित्सा सहायता बुलाएं।",
    "मदद की प्रतीक्षा करते समय हीट स्ट्रोक के शिकार व्यक्ति को ठंडे पानी या बर्फ के पैक का उपयोग करके तेजी से ठंडा करें।",
    "यदि हीट स्ट्रोक से पीड़ित व्यक्ति बेहोश है तो उसे तरल पदार्थ न दें।",
    "कुचलने की चोटों का आकलन करें। भारी चट्टानों से फंसे व्यक्ति को तब तक न हिलाएं जब तक उसका वायुमार्ग अवरुद्ध न हो।",
    "पीड़ित के मुंह और नाक से धूल और मलबा साफ करें ताकि वह सांस ले सके।",
    "सीधे दबाव के साथ सक्रिय रक्तस्राव को नियंत्रित करें।",
    "आगे की चोट को रोकने के लिए पीड़ित को शांत और स्थिर रखें।",
    "मलबे में बचाव दल को अपना स्थान बताने के लिए चिल्लाकर या सीटी बजाकर मदद मांगें।",
    "यदि सुरक्षित हो तभी व्यक्तियों को बचाएं। खतरनाक पानी में प्रवेश करने के बजाय रस्सियों का उपयोग करें।",
    "यदि बचाया गया व्यक्ति सांस नहीं ले रहा है तो तुरंत सीपीआर करें।",
    "गीले कपड़े हटाकर और सूखे कंबल लपेटकर हाइपोथर्मिया का इलाज करें।",
    "तैरते हुए मलबे के कारण होने वाले घावों पर दबाव डालें और उन्हें साफ करें।",
    "बचे हुए लोगों को शांत रखकर मनोवैज्ञानिक प्राथमिक चिकित्सा प्रदान करें।",
    "तुरंत चिकित्सा सहायता बुलाएं। बिजली गिरने के पीड़ितों को छूना सुरक्षित है।",
    "सांस और दिल की धड़कन की जांच करें। यदि पीड़ित प्रतिक्रिया नहीं दे रहा है तो तुरंत सीपीआर शुरू करें।",
    "बिजली प्रवेश और निकास वाले स्थानों पर गंभीर जलन का आकलन करें और इलाज करें।",
    "सदमे, भ्रम या अस्थायी पक्षाघात के संकेतों के लिए पीड़ित की निगरानी करें।",
    "पीड़ित को गर्म रखें और यदि फ्रैक्चर का संदेह हो तो उन्हें स्थिर रखें।"
]

first_aid_mr = [
    "हलण्यापूर्वी स्वतःच्या आणि इतरांच्या जखमा तपासा. गंभीर जखमींना हलवू नका.",
    "रक्तस्राव होणाऱ्या जखमांवर स्वच्छ कापडाने थेट दाब द्या.",
    "फ्रॅक्चर असल्यास हाड सरळ करण्याचा प्रयत्न करू नका.",
    "ढिगाऱ्याखाली अडकल्यास एकट्याने जड वस्तू हलवू नका.",
    "व्यक्ती श्वास घेत नसेल तर सीपीआर (CPR) द्या.",
    "कोणत्याही जखमा त्वरित स्वच्छ पाण्याने आणि साबणाने धुवा.",
    "प्रभावित व्यक्तीला उबदार ठेवून आणि झोपवून शॉकवर उपचार करा.",
    "जखमा निर्जंतुक पट्ट्यांनी झाकून ठेवा.",
    "पुराच्या पाण्याच्या संपर्कात आलेले अन्न किंवा पाणी पिणे टाळा.",
    "हायपोथर्मियाच्या लक्षणांवर लक्ष ठेवा आणि व्यक्तीला हळूहळू उबदार करा.",
    "उडणाऱ्या काचेमुळे झालेल्या जखमांवर त्वरित उपचार करा.",
    "पडणाऱ्या वस्तूंमुळे झालेल्या कोणत्याही संशयित फ्रॅक्चरला स्थिर करा.",
    "एखादी वस्तू शरीरात घुसली असल्यास, ती काढू नका.",
    "क्रश सिंड्रोमच्या लक्षणांसाठी अडकलेल्या किंवा जखमी लोकांवर लक्ष ठेवा.",
    "वादळ ओसरल्यानंतर सुरक्षित बाहेर पडण्यासाठी आपत्कालीन मार्ग मोकळे ठेवा.",
    "उष्णतेमुळे थकवा आल्यास व्यक्तीला त्वरित थंड ठिकाणी हलवा आणि हळूहळू पाणी पाजा.",
    "शरीरावर थंड, ओले कपडे लावा किंवा थंड पाण्याने आंघोळ करा.",
    "उष्माघात (हीट स्ट्रोक) झाल्यास त्वरित वैद्यकीय मदत घ्या.",
    "मदतीची वाट पाहत असताना उष्माघात झालेल्या व्यक्तीला त्वरित थंड करा.",
    "उष्माघाताने ग्रस्त व्यक्ती बेशुद्ध असल्यास त्याला द्रव पदार्थ देऊ नका.",
    "चिरडलेल्या जखमांचे मूल्यांकन करा. जोपर्यंत श्वासनलिका ब्लॉक होत नाही तोपर्यंत अडकलेल्या व्यक्तीला हलवू नका.",
    "पीडितेच्या तोंड आणि नाकातील धूळ आणि ढिगारा साफ करा.",
    "थेट दाबाने सक्रिय रक्तस्राव नियंत्रित करा.",
    "पुढील दुखापत टाळण्यासाठी पीडितेला शांत आणि स्थिर ठेवा.",
    "ढिगाऱ्यात बचाव पथकांना तुमचे स्थान सांगण्यासाठी ओरडून किंवा शिट्टी वाजवून मदत मागा.",
    "सुरक्षित असल्यासच व्यक्तींना वाचवा. धोकादायक पाण्यात जाण्याऐवजी दोरीचा वापर करा.",
    "वाचवलेली व्यक्ती श्वास घेत नसेल तर त्वरित सीपीआर करा.",
    "ओले कपडे काढून आणि कोरडे ब्लँकेट गुंडाळून हायपोथर्मियावर उपचार करा.",
    "तरंगणाऱ्या ढिगाऱ्यामुळे झालेल्या जखमांवर दाब द्या आणि त्या स्वच्छ करा.",
    "वाचलेल्यांना शांत ठेवून मानसिक प्रथमोपचार द्या.",
    "त्वरित वैद्यकीय मदत घ्या. वीज पडलेल्या पीडितांना स्पर्श करणे सुरक्षित आहे.",
    "श्वास आणि हृदयाचे ठोके तपासा. पीडित प्रतिक्रिया देत नसेल तर त्वरित सीपीआर सुरू करा.",
    "वीज प्रवेश आणि बाहेर पडण्याच्या ठिकाणी गंभीर जळजळीचे मूल्यांकन करा आणि उपचार करा.",
    "धक्का, गोंधळ किंवा तात्पुरत्या अर्धांगवायूच्या लक्षणांसाठी पीडितेवर लक्ष ठेवा.",
    "पीडितेला उबदार ठेवा आणि फ्रॅक्चरचा संशय असल्यास त्यांना स्थिर ठेवा."
]

first_aid_ta = [
    "நகரும் முன் உங்களுக்கு மற்றும் மற்றவர்களுக்கு ஏற்பட்ட காயங்களை சரிபார்க்கவும்.",
    "இரத்த கசிவு உள்ள காயங்களில் சுத்தமான துணியால் அழுத்தவும்.",
    "எலும்பு முறிவு ஏற்பட்டால் எலும்பை நேராக்க முயற்சிக்க வேண்டாம்.",
    "இடிபாடுகளில் சிக்கினால் தனியாக கனமான பொருட்களை நகர்த்த வேண்டாம்.",
    "நபர் சுவாசிக்கவில்லை என்றால் CPR வழங்கவும்.",
    "எந்த காயங்களையும் உடனடியாக சுத்தமான தண்ணீர் மற்றும் சோப்பு கொண்டு கழுவவும்.",
    "பாதிக்கப்பட்ட நபரை சூடாகவும், படுக்கவைத்தும் அதிர்ச்சிக்கு சிகிச்சையளிக்கவும்.",
    "காயங்களை கிருமி நீக்கப்பட்ட கட்டுகளால் மூடி வைக்கவும்.",
    "வெள்ள நீரில் தொடர்பு கொண்ட உணவு அல்லது தண்ணீரை உட்கொள்வதைத் தவிர்க்கவும்.",
    "ஹைபோதெர்மியாவின் அறிகுறிகளைக் கவனித்து, நபரை மெதுவாக சூடாக்கவும்.",
    "பறக்கும் கண்ணாடியால் ஏற்படும் வெட்டுக்களுக்கு உடனடியாக சிகிச்சையளிக்கவும்.",
    "விழும் பொருட்களால் ஏற்படும் எந்தவொரு எலும்பு முறிவையும் அசைக்காமல் வைக்கவும்.",
    "உடலில் ஒரு பொருள் குத்தியிருந்தால், அதை அகற்ற வேண்டாம்.",
    "நசுக்கப்படும் நோய்க்குறியின் அறிகுறிகளுக்கு சிக்கியவர்கள் அல்லது காயமடைந்தவர்களை கண்காணிக்கவும்.",
    "புயல் கடந்ததும் பாதுகாப்பான வெளியேற்றத்திற்காக அவசர வழிகளை தெளிவாக வைக்கவும்.",
    "வெப்பச் சோர்வுக்கு நபரை உடனடியாக குளிர்ந்த இடத்திற்கு மாற்றி மெதுவாக தண்ணீர் குடிக்கவும்.",
    "உடலில் குளிர்ந்த, ஈரமான துணிகளைப் பயன்படுத்துங்கள் அல்லது குளிர்ந்த நீரில் குளிக்கவும்.",
    "வெப்ப மாரடைப்புக்கு உடனடியாக மருத்துவ உதவியை அழைக்கவும்.",
    "உதவிக்காக காத்திருக்கும் போது, வெப்ப மாரடைப்பு பாதிக்கப்பட்டவரை வேகமாக குளிர்விக்கவும்.",
    "வெப்ப மாரடைப்பால் பாதிக்கப்பட்ட நபர் மயக்கமடைந்திருந்தால் அவருக்கு திரவங்களை கொடுக்க வேண்டாம்.",
    "நசுக்கப்பட்ட காயங்களை மதிப்பிடவும். காற்றுப்பாதை அடைக்கப்படாவிட்டால் சிக்கியவரை நகர்த்த வேண்டாம்.",
    "பாதிக்கப்பட்டவரின் வாய் மற்றும் மூக்கில் உள்ள குப்பைகளை அகற்றவும்.",
    "நேரடி அழுத்தம் மூலம் எந்தவொரு செயலில் உள்ள இரத்த கசிவையும் கட்டுப்படுத்தவும்.",
    "மேலும் காயத்தைத் தடுக்க பாதிக்கப்பட்டவரை அமைதியாகவும் அசையாமலும் வைக்கவும்.",
    "மீட்பு குழுக்களுக்கு உங்கள் இருப்பிடத்தை தெரிவிக்க கத்தி அல்லது விசில் மூலம் உதவி கேட்கவும்.",
    "பாதுகாப்பாக இருந்தால் மட்டுமே நபர்களை மீட்கவும். ஆபத்தான தண்ணீரில் நுழைவதற்குப் பதிலாக கயிறுகளைப் பயன்படுத்தவும்.",
    "மீட்கப்பட்ட நபர் சுவாசிக்கவில்லை என்றால் உடனடியாக CPR செய்யவும்.",
    "ஈரமான ஆடைகளை அகற்றி, உலர்ந்த போர்வைகளை போர்த்தி ஹைபோதெர்மியாவுக்கு சிகிச்சையளிக்கவும்.",
    "மிதக்கும் குப்பைகளால் ஏற்படும் காயங்களுக்கு அழுத்தம் கொடுத்து அவற்றை சுத்தம் செய்யவும்.",
    "உயிர் பிழைத்தவர்களை அமைதியாக வைத்து உளவியல் முதலுதவி வழங்கவும்.",
    "உடனடியாக மருத்துவ உதவியை அழைக்கவும். மின்னல் தாக்கியவர்களை தொடுவது பாதுகாப்பானது.",
    "சுவாசம் மற்றும் இதயத் துடிப்பை சரிபார்க்கவும். பாதிக்கப்பட்டவர் பதிலளிக்கவில்லை என்றால் உடனடியாக CPR தொடங்கவும்.",
    "மின்னல் நுழைந்து வெளியேறிய இடங்களில் கடுமையான தீக்காயங்களை மதிப்பிட்டு சிகிச்சையளிக்கவும்.",
    "அதிர்ச்சி, குழப்பம் அல்லது தற்காலிக முடக்கம் போன்ற அறிகுறிகளுக்கு பாதிக்கப்பட்டவரை கண்காணிக்கவும்.",
    "பாதிக்கப்பட்டவரை சூடாக வைக்கவும், எலும்பு முறிவு ஏற்பட்டால் அவர்களை அசையாமல் வைக்கவும்."
]

first_aid_te = [
    "కదలడానికి ముందు మీకు మరియు ఇతరులకు గాయాలు ఉన్నాయో లేదో తనిఖీ చేయండి.",
    "రక్తస్రావం అవుతున్న గాయాలపై శుభ్రమైన వస్త్రంతో నేరుగా ఒత్తిడి చేయండి.",
    "పగుళ్లు ఉన్నట్లు అనుమానం ఉంటే ఎముకను సరిచేయడానికి ప్రయత్నించకండి.",
    "శిధిలాల కింద చిక్కుకుంటే ఒంటరిగా బరువైన వస్తువులను తరలించవద్దు.",
    "వ్యక్తి శ్వాస తీసుకోకపోతే CPR అందించండి.",
    "ఏదైనా గాయాలను వెంటనే శుభ్రమైన నీరు మరియు సబ్బుతో కడగాలి.",
    "బాధిత వ్యక్తిని వెచ్చగా ఉంచి, పడుకోబెట్టి షాక్‌కు చికిత్స చేయండి.",
    "గాయాలను శుభ్రమైన బ్యాండేజీలతో కప్పి ఉంచండి.",
    "వరద నీటితో సంబంధం ఉన్న ఆహారం లేదా నీటిని తినడం మానుకోండి.",
    "హైపోథెర్మియా సంకేతాలను గమనించండి మరియు వ్యక్తిని నెమ్మదిగా వెచ్చగా చేయండి.",
    "ఎగురుతున్న గాజు వల్ల కలిగే కోతలకు వెంటనే చికిత్స చేయండి.",
    "పడే వస్తువుల వల్ల కలిగే పగుళ్లను కదలకుండా చేయండి.",
    "శరీరంలో ఏదైనా వస్తువు గుచ్చుకుంటే, దాన్ని తీసివేయవద్దు.",
    "క్రష్ సిండ్రోమ్ సంకేతాల కోసం చిక్కుకున్న లేదా గాయపడిన వారిని పర్యవేక్షించండి.",
    "తుఫాను వెలిసిన తర్వాత సురక్షితమైన తరలింపు కోసం అత్యవసర మార్గాలను స్పష్టంగా ఉంచండి.",
    "వేడి అలసట కోసం వ్యక్తిని వెంటనే చల్లని ప్రదేశానికి తరలించి నెమ్మదిగా నీరు త్రాగించండి.",
    "శరీరంపై చల్లని, తడి బట్టలు వేయండి లేదా చల్లటి స్నానం చేయండి.",
    "వడదెబ్బ కోసం వెంటనే వైద్య సహాయం తీసుకోండి.",
    "సహాయం కోసం ఎదురుచూస్తున్నప్పుడు వడదెబ్బ బాధితుడిని వేగంగా చల్లబరచండి.",
    "వడదెబ్బతో బాధపడుతున్న వ్యక్తి అపస్మారక స్థితిలో ఉంటే ద్రవాలు ఇవ్వవద్దు.",
    "నలిగిన గాయాలను అంచనా వేయండి. వాయుమార్గం మూసుకుపోతే తప్ప చిక్కుకున్న వ్యక్తిని తరలించవద్దు.",
    "బాధితుడి నోరు మరియు ముక్కు నుండి చెత్తను తొలగించండి.",
    "ప్రత్యక్ష ఒత్తిడితో ఏదైనా క్రియాశీల రక్తస్రావాన్ని నియంత్రించండి.",
    "మరింత గాయాన్ని నివారించడానికి బాధితుడిని ప్రశాంతంగా మరియు కదలకుండా ఉంచండి.",
    "రెస్క్యూ బృందాలకు మీ స్థానాన్ని తెలియజేయడానికి అరిచి లేదా ఈల వేసి సహాయం కోసం అడగండి.",
    "సురక్షితమైతే మాత్రమే వ్యక్తులను రక్షించండి. ప్రమాదకరమైన నీటిలోకి ప్రవేశించే బదులు తాడులను ఉపయోగించండి.",
    "రక్షించబడిన వ్యక్తి శ్వాస తీసుకోకపోతే వెంటనే CPR చేయండి.",
    "తడి బట్టలు తీసివేసి పొడి దుప్పట్లు కప్పి హైపోథెర్మియాకు చికిత్స చేయండి.",
    "తేలియాడే చెత్త వల్ల కలిగే గాయాలపై ఒత్తిడి చేసి వాటిని శుభ్రం చేయండి.",
    "ప్రాణాలతో బయటపడిన వారిని ప్రశాంతంగా ఉంచి మానసిక ప్రథమ చికిత్స అందించండి.",
    "వెంటనే వైద్య సహాయం కోసం పిలవండి. పిడుగుపాటు బాధితులను తాకడం సురక్షితం.",
    "శ్వాస మరియు హృదయ స్పందనను తనిఖీ చేయండి. బాధితుడు స్పందించకపోతే వెంటనే CPR ప్రారంభించండి.",
    "పిడుగు ప్రవేశించిన మరియు బయటకు వచ్చిన ప్రదేశాలలో తీవ్రమైన కాలిన గాయాలను అంచనా వేసి చికిత్స చేయండి.",
    "షాక్, గందరగోళం లేదా తాత్కాలిక పక్షవాతం సంకేతాల కోసం బాధితుడిని పర్యవేక్షించండి.",
    "బాధితుడిని వెచ్చగా ఉంచండి మరియు పగుళ్లు ఉంటే వారిని కదలకుండా చేయండి."
]

with open('/app/src/main/java/com/example/ui/translation/Translation.kt', 'r') as f:
    content = f.read()

def insert_dict(map_name, en_list, translated_list):
    global content
    
    insert_string = ""
    for en, tr in zip(en_list, translated_list):
        insert_string += f'        "{en}" to "{tr}",\n'
    
    pattern = f'val {map_name} = mapOf\\((.*?)\\n    \\)'
    match = re.search(pattern, content, re.DOTALL)
    if match:
        inner = match.group(1)
        new_inner = inner.rstrip()
        if not new_inner.endswith(','):
            new_inner += ','
        new_inner += '\n' + insert_string.rstrip()
        new_content = content.replace(match.group(0), f'val {map_name} = mapOf({new_inner}\n    )')
        content = new_content

insert_dict('hiMap', first_aid_en, first_aid_hi)
insert_dict('mrMap', first_aid_en, first_aid_mr)
insert_dict('taMap', first_aid_en, first_aid_ta)
insert_dict('teMap', first_aid_en, first_aid_te)

with open('/app/src/main/java/com/example/ui/translation/Translation.kt', 'w') as f:
    f.write(content)

print("Translations injected successfully")
