package com.example.data

data class Calamity(
    val id: String,
    val name: String,
    val iconName: String,
    val steps: List<String>,
    val firstAidGuide: List<String>,
    val emergencyContacts: List<String>,
    val helplineNo: String,
    val infographics: List<String> = emptyList()
)

object CalamityRepository {
    val calamities = listOf(
        Calamity(
            id = "earthquake",
            name = "Earthquake",
            iconName = "warning",
            steps = listOf(
                "Drop down onto your hands and knees immediately. This position protects you from falling but allows you to move if necessary.",
                "Cover your head and neck (and your entire body if possible) under a sturdy table or desk. If there is no shelter nearby, get down near an interior wall or next to low-lying furniture that won't fall on you.",
                "Hold on to your shelter (or to your head and neck) until the shaking stops. Be prepared to move with your shelter if the shaking shifts it.",
                "If you are indoors, stay there. Do not run outside or to other rooms during shaking to avoid falling debris.",
                "If outdoors, move away from buildings, streetlights, and utility wires. Once in the open, stay there until the shaking stops."
            ),
            firstAidGuide = listOf(
                "Check for injuries on yourself and those around you before moving. Do not move seriously injured individuals unless they are in immediate danger.",
                "Apply direct pressure to bleeding wounds using a clean cloth or bandage. Elevate the injured area if possible to reduce blood flow.",
                "For suspected fractures, do not attempt to realign the bone. Immobilize the injured area using a splint or padding.",
                "If someone is trapped under debris, do not attempt to move heavy items alone. Call out for help and try to create breathing space without destabilizing the structure.",
                "Provide CPR if the person is unresponsive and not breathing normally. Ensure the airway is clear before beginning chest compressions."
            ),
            emergencyContacts = listOf(
                "National Emergency Disaster Relief: 1078",
                "Fire Department Search & Rescue: 101",
                "Local Ambulance Services: 102"
            ),
            helplineNo = "112",
            infographics = listOf("https://images.unsplash.com/photo-1620645607383-7c870233bfa8?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1571217036496-c187e1488c03?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "flood",
            name = "Flood",
            iconName = "water",
            steps = listOf(
                "Move immediately to higher ground or a designated safe zone. Do not wait for instructions to move if the water levels are rising rapidly.",
                "Avoid walking or driving through floodwaters. Just 6 inches of moving water can knock you down, and 12 inches can sweep your vehicle away.",
                "Turn off utilities at the main switches or valves if instructed to do so. Disconnect electrical appliances and do not touch electrical equipment if you are wet or standing in water.",
                "If trapped in a building, move to the highest level. Do not climb into a closed attic as you may become trapped by rising floodwater; only go to the roof if necessary.",
                "Stay informed by monitoring local radio or television for updates and emergency instructions. Heed evacuation orders immediately."
            ),
            firstAidGuide = listOf(
                "Clean any wounds immediately with clean, uncontaminated water and soap. Floodwaters carry hazardous bacteria and chemicals that can cause severe infections.",
                "Treat for shock by keeping the affected person warm and lying down. Elevate their legs slightly if there are no spinal injuries.",
                "Keep wounds covered with sterile bandages. If redness, swelling, or excessive pain occurs, seek medical attention as soon as possible.",
                "Avoid consuming food or water that has come into contact with floodwater. Dehydration is common, so ensure you drink only bottled or boiled water.",
                "Watch for signs of hypothermia, such as shivering, confusion, and slurred speech. Warm the person slowly using dry blankets and warm, non-alcoholic liquids."
            ),
            emergencyContacts = listOf(
                "Flood Control Room: 1070",
                "Coast Guard & Marine Rescue: 1093",
                "Medical Emergency Services: 102"
            ),
            helplineNo = "1078",
            infographics = listOf("https://images.unsplash.com/photo-1547683905-f686c993aae5?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1428592953211-077101b2021b?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "cyclone",
            name = "Cyclone",
            iconName = "cyclone",
            steps = listOf(
                "Secure your home by closing and boarding up all windows and doors. Move lightweight objects indoors and tie down anything that could become a projectile.",
                "Turn off the main power and gas supply to prevent electrical hazards and gas leaks during severe structural damage.",
                "Take refuge in a small, windowless interior room or hallway on the lowest level of your home. Use mattresses or sturdy furniture to protect yourself from flying glass and debris.",
                "Prepare an emergency kit with enough non-perishable food, bottled water, medications, and battery-powered radios to last at least 72 hours.",
                "Do not be deceived by the 'eye' of the storm. If the wind suddenly drops, stay indoors, as severe winds will resume rapidly from the opposite direction."
            ),
            firstAidGuide = listOf(
                "Treat cuts and lacerations caused by flying glass immediately. Clean with antiseptic and apply a tight, sterile dressing.",
                "Immobilize any suspected fractures caused by falling objects. Use rigid materials like cardboard or magazines as temporary splints.",
                "If an object is impaled in the body, DO NOT remove it. Pack bulky dressings around the object to stabilize it and control bleeding until professional help arrives.",
                "Monitor those trapped or injured for signs of crush syndrome, which can occur after prolonged compression of muscle tissue.",
                "Keep emergency pathways clear of hazardous debris to ensure safe evacuation once the storm has fully passed."
            ),
            emergencyContacts = listOf(
                "Cyclone Warning Division: 1070",
                "Disaster Management Authority: 1078",
                "Police Emergency: 100"
            ),
            helplineNo = "1070",
            infographics = listOf("https://images.unsplash.com/photo-1527482797697-8795b05a13fe?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1502484918737-1e5b8889ffcb?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "heatwave",
            name = "Heatwave",
            iconName = "sunny",
            steps = listOf(
                "Stay indoors in the coolest room of your house, preferably with air conditioning or a fan. Avoid going outside during the hottest parts of the day (usually 11 AM to 4 PM).",
                "Drink plenty of water consistently throughout the day, even if you don't feel thirsty. Avoid alcohol, caffeine, and highly sugary drinks as they can accelerate dehydration.",
                "Wear loose-fitting, light-colored, and breathable cotton clothing. Protect your head and face with a wide-brimmed hat or umbrella if you must go outside.",
                "Never leave children, the elderly, or pets in closed, parked vehicles. Temperatures inside a car can reach lethal levels within minutes.",
                "Eat light, cool meals rather than heavy, hot food. Foods with high water content, like fruits and salads, help keep you hydrated and cool."
            ),
            firstAidGuide = listOf(
                "For heat exhaustion (heavy sweating, weakness, cold/clammy skin, nausea): move the person to a cooler environment immediately and have them sip water slowly.",
                "Apply cool, wet cloths to the body or take a cool bath. Loosen clothing and fan the person to lower their body temperature.",
                "For heat stroke (high body temperature, hot/red/dry skin, confusion, loss of consciousness): call for medical help IMMEDIATELY. This is a life-threatening emergency.",
                "While waiting for help, move the heat stroke victim to the shade and rapidly cool them using cold water or ice packs placed on the neck, armpits, and groin.",
                "Do not give fluids to a person suffering from heat stroke if they are unconscious or vomiting, as they may choke."
            ),
            emergencyContacts = listOf(
                "Ambulance/Medical Emergency: 102",
                "National Health Helpline: 104"
            ),
            helplineNo = "102",
            infographics = listOf("https://images.unsplash.com/photo-1504369408422-0d65a6c986c0?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1546252985-78e7f84752c1?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "landslide",
            name = "Landslide",
            iconName = "warning",
            steps = listOf(
                "Listen to local news and emergency broadcasts for warnings of heavy rainfall and potential landslides in your area.",
                "Move away from the path of a landslide or debris flow as quickly as possible. The flow of mud and rocks can move faster than you can run.",
                "If escape is not possible, curl into a tight ball and protect your head and neck. Hide under a sturdy piece of furniture if you are indoors.",
                "Avoid river valleys and low-lying areas during heavy rainstorms, as these are natural pathways for debris flows.",
                "After the landslide, stay away from the slide area. Additional slides can occur, and the ground may be unstable for days."
            ),
            firstAidGuide = listOf(
                "Assess for crush injuries and severe trauma. Do not attempt to move someone trapped by heavy rocks or mud unless their airway is blocked.",
                "Clear dirt and debris from the victim's mouth and nose to ensure they can breathe.",
                "Control any active bleeding with direct pressure, but avoid applying pressure to suspected broken bones in the ribcage or skull.",
                "Keep the victim calm and still to prevent further injury, especially if a spinal injury is suspected from the impact.",
                "Signal for help by shouting, whistling, or flashing a light so search and rescue teams can locate you in the debris."
            ),
            emergencyContacts = listOf(
                "Disaster Management Helpline: 1078",
                "Fire and Rescue: 101"
            ),
            helplineNo = "112",
            infographics = listOf("https://images.unsplash.com/photo-1621539206686-eec003cb0673?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1587813083163-e38fae1f7626?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "tsunami",
            name = "Tsunami",
            iconName = "water",
            steps = listOf(
                "If you feel a strong earthquake near the coast, or observe the ocean receding rapidly, move immediately to higher ground. Do not wait for an official warning.",
                "Evacuate by foot if possible, as roads may be jammed or destroyed. Move at least 100 feet above sea level or 2 miles inland.",
                "If you are unable to reach higher ground, go to the upper floor or roof of a sturdy, reinforced concrete building.",
                "Never go to the beach to watch a tsunami come in. If you can see the wave, you are too close to escape it.",
                "Stay away from the coast until local officials issue an 'All Clear'. Tsunamis are a series of waves, and subsequent waves can be larger and more destructive."
            ),
            firstAidGuide = listOf(
                "Rescue individuals only if it is safe for you to do so. Use ropes or flotation devices rather than entering the hazardous, debris-filled water.",
                "Perform CPR immediately if a rescued victim is unresponsive and has inhaled water. Ensure the airway is clear of mud or sand.",
                "Treat hypothermia by removing wet clothing and wrapping the victim in dry blankets. Huddle together to share body heat if necessary.",
                "Apply pressure to lacerations caused by floating debris. Infection is highly likely, so clean wounds thoroughly with bottled water.",
                "Provide psychological first aid by keeping survivors calm and reassuring them, as the trauma of a tsunami can induce severe shock."
            ),
            emergencyContacts = listOf(
                "Tsunami Warning Center: 1078",
                "Coast Guard Emergency: 1093"
            ),
            helplineNo = "112",
            infographics = listOf("https://images.unsplash.com/photo-1522031102905-3eab4b13bd94?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1532453288672-3a27e9be9efd?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "thunderstorm",
            name = "Lightning",
            iconName = "cyclone",
            steps = listOf(
                "Seek shelter immediately in a substantial building or a hard-topped metal vehicle with the windows rolled up.",
                "If you are caught outdoors with no shelter, avoid tall, isolated objects like trees, poles, and open fields.",
                "If you are in a group, spread out to prevent multiple casualties from a single lightning strike.",
                "Adopt the lightning crouch: squat low to the ground on the balls of your feet, tuck your head between your knees, and cover your ears. Do not lie flat.",
                "Avoid using corded electronics and plumbing indoors, as lightning can travel through electrical wiring and metal pipes."
            ),
            firstAidGuide = listOf(
                "Call for medical help immediately. Lightning strike victims do not carry an electrical charge and are safe to touch and treat.",
                "Check for breathing and a heartbeat. Lightning often causes cardiac arrest. Begin CPR immediately if the victim is unresponsive and pulseless.",
                "Assess for and treat severe burns where the lightning entered and exited the body.",
                "Monitor the victim for signs of shock, confusion, or temporary paralysis, which are common after a strike.",
                "Keep the victim warm and immobilize them if you suspect they have suffered fractures or spinal injuries from being thrown by the blast."
            ),
            emergencyContacts = listOf(
                "Emergency Services: 112",
                "Local Ambulance: 102"
            ),
            helplineNo = "102",
            infographics = listOf("https://images.unsplash.com/photo-1605662706598-f58c735d4605?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1472145246862-b24cf25c4a36?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "wildfire",
            name = "Wildfire",
            iconName = "warning",
            steps = listOf(
                "Evacuate immediately if instructed to do so by local authorities. Follow designated evacuation routes.",
                "If trapped, find a body of water or a cleared area with no vegetation. Lie flat and cover your body with wet clothing or soil.",
                "Close all windows and doors to prevent drafts and reduce the entry of smoke and embers.",
                "Remove flammable materials (like curtains or outdoor furniture) away from the house if time permits.",
                "Protect your lungs by breathing through a damp cloth, or use an N95 mask to filter out dangerous smoke and ash particles."
            ),
            firstAidGuide = listOf(
                "Move the victim to a safe area with fresh air immediately. Smoke inhalation can be fatal.",
                "Assess for and treat severe burn injuries. Cool burns with clean, cool water for at least 10 minutes. Do NOT use ice.",
                "Cover burns loosely with a sterile, non-fluffy dressing or clean plastic wrap. Do not apply creams or ointments.",
                "Monitor for breathing difficulties, coughing, or wheezing. Provide oxygen if available and trained to do so.",
                "Keep the person calm and treat for shock while waiting for emergency medical services."
            ),
            emergencyContacts = listOf(
                "Fire Department: 101",
                "Ambulance: 102",
                "Forest Department Helpline: 1926"
            ),
            helplineNo = "101",
            infographics = listOf("https://images.unsplash.com/photo-1600329092404-f58c49e7b2ff?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1599818815124-78335f458145?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "avalanche",
            name = "Avalanche",
            iconName = "warning",
            steps = listOf(
                "If caught in an avalanche, try to move to the side of the slide. Use a swimming motion to stay near the surface of the snow.",
                "As the snow slows down, cup your hands over your mouth and nose to create an air pocket.",
                "Keep calm and conserve oxygen. Yell for help only when you hear rescuers above you, as snow muffles sound.",
                "If you witness an avalanche, do not enter the danger zone. Mark where you last saw the victims and call for help.",
                "Use avalanche beacons, probes, and shovels to locate and dig out victims as quickly as possible."
            ),
            firstAidGuide = listOf(
                "Clear the victim's airway of snow and ice immediately. Begin CPR if they are not breathing.",
                "Treat for severe hypothermia. Move the victim to a warm environment and replace wet clothing with dry blankets.",
                "Handle the victim gently, as rough movement can cause cardiac arrest in severely hypothermic individuals.",
                "Assess for traumatic injuries, such as broken bones or spinal damage, caused by being tumbled in the snow.",
                "Apply warm packs to the neck, armpits, and groin, but avoid directly heating the extremities to prevent shock."
            ),
            emergencyContacts = listOf(
                "Mountain Rescue: 112",
                "Local Ambulance: 102"
            ),
            helplineNo = "112",
            infographics = listOf("https://images.unsplash.com/photo-1518779578993-ec3579fee39f?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1603598586071-7f9fc9ebdd0c?q=80&w=800&auto=format&fit=crop")
        ),
        Calamity(
            id = "volcanic_eruption",
            name = "Volcanic Eruption",
            iconName = "warning",
            steps = listOf(
                "Follow evacuation orders issued by local authorities immediately. Avoid areas downwind and river valleys downstream of the volcano.",
                "Protect yourself from falling ash by wearing long-sleeved shirts, long pants, goggles, and an N95 mask.",
                "Stay indoors with windows, doors, and ventilation systems closed until the ash settles.",
                "If driving, keep car windows rolled up and avoid driving in heavy ash fall, as it can clog the engine and obscure vision.",
                "Avoid roofs as heavy ash accumulation can cause buildings to collapse. Clear ash carefully if safe to do so."
            ),
            firstAidGuide = listOf(
                "Treat respiratory irritation by moving the person to a clear environment. Provide oxygen if they have extreme difficulty breathing.",
                "Flush eyes with generous amounts of clean water if irritated by volcanic ash. Do not rub the eyes, as ash particles are sharp and abrasive.",
                "Treat burns from hot ash or lava by cooling the area with clean water. Cover with sterile dressings.",
                "Monitor for signs of shock or dehydration, especially if the eruption is accompanied by extreme heat.",
                "Clean any open wounds meticulously, as volcanic ash can cause severe infection and tissue damage."
            ),
            emergencyContacts = listOf(
                "Disaster Management: 1078",
                "Medical Emergency: 102"
            ),
            helplineNo = "1078",
            infographics = listOf("https://images.unsplash.com/photo-1543781250-0a2a514d5ce2?q=80&w=800&auto=format&fit=crop", "https://images.unsplash.com/photo-1628172901594-ce7ab5363fb4?q=80&w=800&auto=format&fit=crop")
        )
    )
}
