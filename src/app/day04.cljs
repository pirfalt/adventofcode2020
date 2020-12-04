(ns app.day04
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in")

;; string -> (("ecl:gry" "pid:860033327" ...) ...)
(defn raw-passports [input]
  (let [lines (s/split input #"\n\n")]
    (map #(->>
           ;; split line on whitespace
           (s/split %1 #"\s")
           ;; only keep non blank values
           (filter (complement s/blank?)))
         lines)))

;; ("ecl:gry" "pid:860033327" ...) -> {"ecl" "gry", "pid" "860033327" ...}
(defn parse-raw-passport [raw-passport]
  (let [kvs (map #(s/split %1 ":") raw-passport)]
    (into {} kvs)))


;; byr (Birth Year)
;; iyr (Issue Year)
;; eyr (Expiration Year)
;; hgt (Height)
;; hcl (Hair Color)
;; ecl (Eye Color)
;; pid (Passport ID)
;; cid (Country ID)
(defn validate [pass]
  (and
   (contains? pass "byr")
   (contains? pass "iyr")
   (contains? pass "eyr")
   (contains? pass "hgt")
   (contains? pass "hcl")
   (contains? pass "ecl")
   (contains? pass "pid")
   ;; (contains? pass "cid")
   ))

(comment
  (->> example
       raw-passports
       (map parse-raw-passport)
       (filter validate)
      ;;  (count)
       )
  ;;
  )

(defn one [input]
  (->> (raw-passports input)
       (map parse-raw-passport)
       (filter validate)
       count))


;; ---------------------------------------------------------------


(defn parse-int [in] (js/parseInt in))

(defn parse-year [input]
  (->> input
       (re-matches #"(\d{4})")
       ((fn [[_ year]] year))
       parse-int))

;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
;; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
;; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
;; hgt (Height) - a number followed by either cm or in:
;; If cm, the number must be at least 150 and at most 193.
;; If in, the number must be at least 59 and at most 76.
;; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
;; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
;; pid (Passport ID) - a nine-digit number, including leading zeroes.
;; cid (Country ID) - ignored, missing or not.)
(defn validate-2 [pass]
  (and
   (some->> (pass "byr")
            parse-year
            ((fn [year]
               (and (>= year 1920) (<= year 2002)))))
   (some->> (pass "iyr")
            parse-year
            ((fn [year]
               (and (>= year 2010) (<= year 2020)))))
   (some->> (pass "eyr")
            parse-year
            ((fn [year]
               (and (>= year 2020) (<= year 2030)))))
   (some->> (pass "hgt")
            (re-matches #"(\d+)((cm)|(in))")
            ((fn [[_ nr-in unit]]
               (let [nr (parse-int nr-in)]
                 (case unit
                   "cm" (and (>= nr 150) (<= nr 193))
                   "in" (and (>= nr 59) (<= nr 76))
                   false)))))
   (some->> (pass "hcl")
            (re-matches #"#[0-9a-f]{6}"))
   (some->> (pass "ecl")
            (re-matches #"(amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth)"))
   (some->> (pass "pid")
            (re-matches #"\d{9}"))
   ;  (contains? pass "cid")
   true))

(comment
  (def invalid-pass "eyr:1972 cid:100
    hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

    iyr:2019
    hcl:#602927 eyr:1967 hgt:170cm
    ecl:grn pid:012533040 byr:1946

    hcl:dab227 iyr:2012
    ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

    hgt:59cm ecl:zzz
    eyr:2038 hcl:74454a iyr:2023
    pid:3556412378 byr:2007")

  (def valid-pass "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
    hcl:#623a2f

    eyr:2029 ecl:blu cid:129 byr:1989
    iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

    hcl:#888785
    hgt:164cm byr:2001 iyr:2015 cid:88
    pid:545766238 ecl:hzl
    eyr:2022

    iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719")
  ;; => #'app.day04/valid-pass

  (->> valid-pass
       raw-passports
       (map parse-raw-passport)
       (filter validate-2)
       ;;
       )

  (->> invalid-pass
       raw-passports
       (map parse-raw-passport)
       (filter validate-2)
       ;;
       )
  ;;
  )

(defn two [input]
  (->> (raw-passports input)
       (map parse-raw-passport)
       (filter validate-2)
       count))

(defn run []
  (print (one (reader/read-file "./data/04.txt")))
  (print (two (reader/read-file "./data/04.txt")))
  nil)
(run)
