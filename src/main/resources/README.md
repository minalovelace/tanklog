# Tanklog

Tiny homemade project with a simple **DSL** to produce a functional pdf as the output
for your log book to keep track of the refueling and consumption of your car.

## Dear user

Thank you for using my small homemade program. With this little helper,
you're now able to produce a simple yet compelling *pdf*. This *pdf* will contain
informations about your car's refueling history. It will also contain informations
about bringing your car to the garage, provided you entered them. Entering informations
is quite easy and will be explained in this document.

Providing informations, which will appear in the *pdf*, is straight forward. Simply
write the informations in its own **DSL** in a text file, which has to be placed in the
*input* folder next to the jar file. Double clicking the jar file produces the *pdf*
file in the *output* folder. All you need to know is how to write the text file.
Therefore, you have to learn a simple **DSL**, which will be discussed in the next section.

## The DSL

The **DSL** specifies simple keywords, which can be used for the description of the
information used for the *pdf*. We will go through every single keyword and its usage.

With the keyword **carname**, you can specify the name of the car. The input till
the end of the line will be used as the name of the car.

```custom
carname Red hot rod
```

With the keyword **numberplate**, you can specify the content of your number plate. As
before, the input till the end of the line will be used.

```custom
numberplate AB - C 1234
```

With the keyword **garage**, you can enter when your car was in the car shop and
what was done. You can enter dates in three different formats. Everything after
the date till the end of the line will be taken as information about what was done.

```custom
garage YYYY-MM-DD Winter tires mounted
```

```custom
garage DD.MM.YYYY Winter tires mounted
```

```custom
garage MM/DD/YYYY Winter tires mounted
```

Since an oil change is something special, it must be entered separately. Please use
the syntax

```custom
oilchange YYYY-MM-DD 12,345 km
```

With the keyword **entry**, you can enter when you refueled your car, the price for
a liter, the liters itself and the mileage in kilometers. Euro, liter an kilometer
can be abbreviated: **eur**, **l**, and **km**.

```custom
entry YYYY-MM-DD 45.98 euro 35.99 liter 23,420 kilometer
```

With the keywords **airpressure** and **fullyladen** or **halfladen**, you can specify
the air pressure for your wheels when your vehicle is full or half laden.

```custom
airpressure fullyladen 2.4
```

or

```custom
airpressure halfladen 2.5
```

You can even specify the air pressure of your spare wheel with

```custom
airpressure sparewheel 4.2
```

With the keywords **oilchange** and **with** or **without** **filter**, you can specify
the amount of oil in liters to be used for an oil change.

```custom
oilchange without filter 3.5
```

or

```custom
oilchange with filter 4.5
```

With the keyword **purchasedetail**, you can specify the date of purchase
and the mileage.

```custom
purchasedetail 2016-02-15 51,515 km
```

With the keyword **note**, you can write custom notes, which will appear at the end of
the *pdf*. As before, the input till the end of the line will be used.

```custom
note Some noteworthy words
```

With the keyword **wook** is a special one. It's a shortcut to write when oil and
water were checked. Wook stands for **w**ater and **o**il **ok**ay.

```custom
wook YYYY-MM-DD
```

## The output

The output will be produced from the latest text file in the *input* folder. Double
clicking on the *jar* produces a *pdf* file in the *output* folder if your input was
valid. For invalid input, a log file will be written to the *output* folder.

## Open issues and missing features

A nice feature would be to have a procedure to pretty print the input text file.
The result will be written to the *output* folder.

Another open feature is the specification of the language and the number formats.
This is also the biggest flaw in the whole project, since the produced LaTeX file
is completely in German. This should be refactored and moved to separate files and
class(es) for the logic for the localization. The user should be able to specify
their language, number formats, and units.

Actually, this project is only running on a Unix-like system, where LaTeX is installed
under **/Library/TeX/texbin/pdflatex**. This has to be adopted for other systems like Windows.